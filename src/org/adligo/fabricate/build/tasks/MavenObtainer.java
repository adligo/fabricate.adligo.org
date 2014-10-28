package org.adligo.fabricate.build.tasks;

import org.adligo.fabricate.common.DependencyTypeHelper;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.external.DefaultLocalRepositoryPathBuilder;
import org.adligo.fabricate.external.RepositoryDownloader;
import org.adligo.fabricate.xml.io.FabricateDependencies;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.ProjectType;
import org.adligo.fabricate.xml.io.ProjectsType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;
import org.adligo.fabricate.xml.io.library.DependenciesType;
import org.adligo.fabricate.xml.io.library.DependencyType;
import org.adligo.fabricate.xml.io.library.LibraryType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;
import org.adligo.fabricate.xml.io.project.StagesType;
import org.adligo.fabricate.xml_io.LibraryIO;
import org.adligo.fabricate.xml_io.ProjectIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class is a delegates to to other classes that 
 * obtain projects from source control (i.e. git).
 * 
 * @author scott
 *
 */
public class MavenObtainer implements I_FabTask {
  private static PrintStream OUT = System.out;
  private ConcurrentLinkedQueue<ProjectType> projects_ = new ConcurrentLinkedQueue<ProjectType>();
  private ConcurrentLinkedQueue<DependencyTypeHelper> deps_ = new ConcurrentLinkedQueue<DependencyTypeHelper>();
  private CopyOnWriteArraySet<String> libsDone_ = new CopyOnWriteArraySet<String>();
  private CopyOnWriteArraySet<DependencyTypeHelper> uniqueDeps_ = new CopyOnWriteArraySet<DependencyTypeHelper>();
  
  private String stageName_;
  private int projectCount_;
  private AtomicInteger finishedProjectCount_ = new AtomicInteger(0);
  private AtomicInteger depCount_ = new AtomicInteger(0);
  private AtomicInteger finishedDepCount_ = new AtomicInteger(0);
  private String projectsPath_;
  private I_FabContext ctx_;
  private AtomicBoolean finishedProjects_ = new AtomicBoolean(false);
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private volatile Exception lastException_ = null;
  private RepositoryDownloader repoDown_;
  @Override
  public void setup(I_FabContext ctx) {
    ctx_ = ctx;
    FabricateType fab = ctx_.getFabricate();
    FabricateDependencies fabDeps = fab.getDependencies();
    List<String> remoteRepos = fabDeps.getRemoteRepository();
    repoDown_ = new RepositoryDownloader(remoteRepos, 
        new DefaultLocalRepositoryPathBuilder(ctx_.getLocalRepositoryPath(), File.separator), ctx);
    
    projectsPath_ = ctx_.getProjectsPath();
    if (ctx_.isLogEnabled(MavenObtainer.class)) {
      OUT.println("Finding dependencies for projects in " + projectsPath_);
    }
    
    StagesAndProjectsType stageAndProj = fab.getProjectGroup();
    ProjectsType projects = stageAndProj.getProjects();
    
    List<ProjectType> projTypes =  projects.getProject();
    projects_.clear();
    projects_.addAll(projTypes);
    projectCount_ = projTypes.size();
    finishedProjectCount_.set(0);
    finished_.set(false);
  }

  @Override
  public void run() {
    try {
      ProjectType project = projects_.poll();
      
      String fabricateDir = ctx_.getFabricateDirPath();
      while (project != null) {
        String projectName = project.getName();
        File projectXml = new File(projectsPath_ + File.separator + projectName + File.separator +
            "project.xml");
        if (!projectXml.exists()) {
          lastException_ = new FileNotFoundException(projectXml.getAbsolutePath());
          return;
        }
        if (ctx_.isLogEnabled(MavenObtainer.class)) {
          OUT.println("Reading " + projectXml);
        }
        FabricateProjectType fabProject = ProjectIO.parse(projectXml);
        StagesType stagesType = fabProject.getStages();
        List<String> stages = stagesType.getStage();
        if (stages.contains(stageName_)) {
          DependenciesType deps = fabProject.getDependencies();
          if (deps != null) {
            List<String> libs =  deps.getLibrary();
            if (libs != null) {
              for (String lib: libs) {
                if (ctx_.isLogEnabled(MavenObtainer.class)) {
                  OUT.println("project " + projectName + " has library " + lib);
                }
                addLibrary(fabricateDir, lib);
              }
            }
            List<DependencyType> depsList = deps.getDependency();
            for (DependencyType dep: depsList) {
              uniqueDeps_.add(new DependencyTypeHelper(dep));
            }
          }
        }
        finishedProjectCount_.incrementAndGet();
        
        project = projects_.poll();
      }
      if (finishedProjectCount_.get() == projectCount_) {
        if (!finishedProjects_.get()) {
          synchronized (this) {
            if (!finishedProjects_.get()) {
              if (ctx_.isLogEnabled(MavenObtainer.class)) {
                OUT.println("Finished finding dependencies for projects in " + projectsPath_ + 
                    System.lineSeparator() + " there are " + uniqueDeps_.size());
              }
              finishedProjects_.set(true);
              deps_.addAll(uniqueDeps_);
              depCount_.set(uniqueDeps_.size());
            }
          }
        }
      }
      
      while (!finishedProjects_.get()) {
        try {
          Thread.sleep(250);
        } catch (InterruptedException x) {
          //do nothing, continue execution
        }
      }
      
      
      DependencyTypeHelper dep = deps_.poll();
      while (dep != null) {
        repoDown_.findOrDownloadAndSha1(dep.getDependencyType());
        finishedDepCount_.incrementAndGet();
        dep = deps_.poll();
      }
      if (finishedDepCount_.get() == depCount_.get()) {
        finished_.set(true);
      }
    } catch (Exception x) {
      lastException_ = x;
    }
  }

  public void addLibrary(String fabricateDir, String lib) throws IOException {
    if (libsDone_.contains(lib)) {
      return;
    }
    libsDone_.add(lib);
    LibraryType libDeps = LibraryIO.parse(new File(fabricateDir + 
        File.separator + "lib" + File.separator + lib + ".xml"));
    DependenciesType depsType = libDeps.getDependencies();
    
    if (depsType != null) {
    List<String> subLibs = depsType.getLibrary();
      for (String subLib: subLibs) {
        //recurse
        addLibrary(fabricateDir, subLib);
      }
      List<DependencyType> deps = depsType.getDependency();
      for (DependencyType dep: deps) {
        uniqueDeps_.add(new DependencyTypeHelper(dep));
      }
    }
  }

  @Override
  public boolean isConcurrent() {
    return true;
  }

  @Override
  public boolean isFinished() {
    if (lastException_ != null) {
       if (ctx_.isLogEnabled(MavenObtainer.class)) {
         OUT.println("MavenObtainer had a exception.");
       }
      return true;
    }
    return finished_.get();
  }

  @Override
  public boolean hadException() {
    if (lastException_ != null) {
      return true;
    }
    return false;
  }

  @Override
  public Exception getException() {
    return lastException_;
  }

  @Override
  public void setStageName(String stageName) {
    stageName_ = stageName;
  }

}
