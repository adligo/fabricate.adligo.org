package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.external.GitCalls;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.GitServerType;
import org.adligo.fabricate.xml.io.ProjectType;
import org.adligo.fabricate.xml.io.ProjectsType;
import org.adligo.fabricate.xml.io.ScmType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class is a delegates to to other classes that 
 * obtain projects from source control (i.e. git).
 * 
 * @author scott
 *
 */
public class GitObtainer implements I_FabStage {
  private static PrintStream OUT = System.out;
  private ConcurrentLinkedQueue<ProjectType> projects_ = new ConcurrentLinkedQueue<ProjectType>();
  private int projectCount_;
  private AtomicInteger finishedCount_ = new AtomicInteger(0);
  private String gitUser_;
  private String gitHost_;
  private String gitPath_;
  private String projectsPath_;
  private I_FabContext ctx_;
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private volatile Exception lastException_ = null;
  private final Semaphore semaphore_ = new Semaphore(1);
  
  @Override
  public void setup(I_FabContext ctx) {
    ctx_ = ctx;
    projectsPath_ = ctx_.getProjectsPath();
    if (ctx_.isLogEnabled(GitObtainer.class)) {
      OUT.println("The project directory is " + projectsPath_);
    }
    FabricateType fab = ctx.getFabricate();
    StagesAndProjectsType stageAndProj = fab.getProjectGroup();
    ProjectsType projects = stageAndProj.getProjects();
    ScmType scm = projects.getScm();
    GitServerType git =  scm.getGit();
    gitUser_ = git.getUser();
    gitHost_ = git.getHostname();
    gitPath_ = git.getPath();
    
    List<ProjectType> projTypes =  projects.getProject();
    projects_.clear();
    if (ctx_.isLogEnabled(GitObtainer.class)) {
      OUT.println("Found " + projTypes.size() + " projects.");
    }
    projects_.addAll(projTypes);
    projectCount_ = projTypes.size();
    finishedCount_.set(0);
    finished_.set(false);
  }

  @Override
  public void run() {
    try {
      FabRunType runType = ctx_.getRunType();
      ProjectType project = projects_.poll();
      
      String gitCommand = ctx_.getArgValue("git");
      
      GitCalls gc = new GitCalls();
      gc.setCtx(ctx_);
      gc.setHostname(gitHost_);
      gc.setRemotePath(gitPath_);
      gc.setUser(gitUser_);
      
      
      while (project != null) {
        String proj = project.getName();
        switch (runType) {
          case DEFAULT:
            cloneAndCheckout(project, gc, proj);
            break;
          case DEVELOPMENT:
            File projectDir = new File(projectsPath_ + File.separator + proj);
            if (!projectDir.exists()) {
              cloneAndCheckout(project, gc, proj);
            } else {
              if ("pull".equals(gitCommand)) {
                if (ctx_.isLogEnabled(GitObtainer.class)) {
                  OUT.println("Starting git pull for " + proj);
                }
                try {
                  gc.pull(proj, projectsPath_);
                } catch (IOException e) {
                  lastException_ = e;
                  return;
                }
                if (ctx_.isLogEnabled(GitObtainer.class)) {
                  OUT.println("Finished git pull for " + proj);
                }
              }
            }
            break;
           default:
             return;
        }
        finishedCount_.incrementAndGet();
        if (finishedCount_.get() == projectCount_) {
          if (!finished_.get()) {
            finish();
          }
          
        }
        project = projects_.poll();
      }
      
    } catch (Exception x) {
      lastException_ = x;
    }
  }

  public void finish() {
    try {
      if (!finished_.get()) {
        semaphore_.acquire();
        if (ctx_.isLogEnabled(GitObtainer.class)) {
          OUT.println("GitObtainer setting finished ");
        }
        finished_.set(true);
        semaphore_.release();
      }
    } catch (InterruptedException x) {
      //do nothing this thread didn't acquire the semaphore
    }
  }

  public void cloneAndCheckout(ProjectType project, GitCalls gc, String proj) {
    if (ctx_.isLogEnabled(GitObtainer.class)) {
      OUT.println("Starting git clone for " + proj);
    }
    try {
      gc.clone(proj, projectsPath_);
    } catch (IOException e) {
      lastException_ = e;
      return;
    }
    if (ctx_.isLogEnabled(GitObtainer.class)) {
      OUT.println("Finished git clone for " + proj);
    }
    String version = project.getVersion();
    if (!StringUtils.isEmpty(version)) {
      if (ctx_.isLogEnabled(GitObtainer.class)) {
        OUT.println("Starting git checkout for " + proj);
      }
      try {
        gc.checkout(proj, projectsPath_, version);
      } catch (IOException e) {
        lastException_ = e;
        return;
      }
      if (ctx_.isLogEnabled(GitObtainer.class)) {
        OUT.println("Finished git checkout for " + proj);
      }
    }
  }

  @Override
  public boolean isFinished() {
    
    if (lastException_ != null) {
      if (ctx_.isLogEnabled(GitObtainer.class)) {
        OUT.println("GitObtainer had a Exception ");
      }
      return true;
    }
    return finished_.get();
  }
  
  public boolean hadException() {
    if (lastException_ == null) {
      return false;
    }
    return true;
  }
  
  public Exception getException() {
    return lastException_;
  }

  @Override
  public boolean isConcurrent() {
    return true;
  }

  @Override
  public void setStageName(String stageName) {
    //ignore
  }

}
