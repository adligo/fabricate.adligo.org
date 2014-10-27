package org.adligo.fabricate.build.tasks;

import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.external.GitCalls;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.GitServerType;
import org.adligo.fabricate.xml.io.ProjectType;
import org.adligo.fabricate.xml.io.ProjectsType;
import org.adligo.fabricate.xml.io.ScmType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class is a delegates to to other classes that 
 * obtain projects from source control (i.e. git).
 * 
 * @author scott
 *
 */
public class GitObtainer implements I_FabTask {
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
  
  @Override
  public void setup(I_FabContext ctx) {
    ctx_ = ctx;
    projectsPath_ = ctx_.getProjectsPath();
    if (ctx_.isLogEnabled(GitObtainer.class)) {
      OUT.println("Git projects are in " + projectsPath_);
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
      OUT.println("Git found " + projTypes.size() + " projects");
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
      if (FabRunType.DEVELOPMENT == runType && gitCommand == null) {
        //nothing to do
        return;
      }
      
      GitCalls gc = new GitCalls();
      gc.setCtx(ctx_);
      gc.setHostname(gitHost_);
      gc.setRemotePath(gitPath_);
      gc.setUser(gitUser_);
      
      
      while (project != null) {
        String proj = project.getName();
        if (!StringUtils.isEmpty(proj)) {
        
          switch (runType) {
            case DEFAULT:
              if (ctx_.isLogEnabled(GitObtainer.class)) {
                OUT.println("Starting git clone for " + proj);
              }
              try {
                gc.clone(proj, projectsPath_);
              } catch (IOException e) {
                lastException_ = e;
                return;
              }
              String version = project.getVersion();
              if (!StringUtils.isEmpty(version)) {
                try {
                  gc.checkout(proj, projectsPath_, version);
                } catch (IOException e) {
                  lastException_ = e;
                  return;
                }
              }
              break;
            case DEVELOPMENT:
                if ("pull".equals(gitCommand)) {
                  try {
                    gc.pull(proj, projectsPath_);
                  } catch (IOException e) {
                    lastException_ = e;
                    return;
                  }
                }
              break;
             default:
               return;
          }
        }
        finishedCount_.incrementAndGet();
        if (finishedCount_.get() == projectCount_) {
          finished_.set(true);
        }
        project = projects_.poll();
      }
    } catch (Exception x) {
      lastException_ = x;
    }
  }

  @Override
  public boolean isFinished() {
    if (lastException_ != null) {
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

}
