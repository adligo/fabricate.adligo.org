package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.shared.ProjectsMemory;
import org.adligo.fabricate.common.DependencyTypeHelper;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.external.DefaultLocalRepositoryPathBuilder;
import org.adligo.fabricate.external.RepositoryDownloader;
import org.adligo.fabricate.xml.io.FabricateDependencies;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.library.DependenciesType;
import org.adligo.fabricate.xml.io.library.DependencyType;
import org.adligo.fabricate.xml.io.library.LibraryType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;
import org.adligo.fabricate.xml.io.project.StagesType;
import org.adligo.fabricate.xml_io.LibraryIO;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class is a delegates to to other classes that 
 * obtain projects from source control (i.e. git).
 * 
 * This task is not participation aware, it doesn't check the project.xml file 
 * for the stage, it always executes.
 * 
 * @author scott
 *
 */
public class MavenObtainer implements I_FabStage {
  private static PrintStream OUT = System.out;
  private ConcurrentLinkedQueue<NamedProject> projects_;
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
  private final Semaphore semaphore4Deps_ = new Semaphore(1);
  private final Semaphore semaphore_ = new Semaphore(1);
  
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
    
    projects_ = ProjectsMemory.getNewProjectDependencyOrder();
    projectCount_ = projects_.size();
    finishedProjectCount_.set(0);
    finished_.set(false);
  }

  @Override
  public void run() {
    try {
      NamedProject project = projects_.poll();
      
      String fabricateDir = ctx_.getFabricateDirPath();
      while (project != null) {
        String projectName = project.getName();
        
        FabricateProjectType fabProject = project.getProject();
        StagesType stagesType = fabProject.getStages();
      
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
        finishedProjectCount_.incrementAndGet();
        
        project = projects_.poll();
      }
      if (finishedProjectCount_.get() == projectCount_) {
        if (!finishedProjects_.get()) {
          finishFindingDeps();
        }
      }
      DefaultLocalRepositoryPathBuilder pathBuilder = new DefaultLocalRepositoryPathBuilder("", "");
      DependencyTypeHelper dep = deps_.poll();
      while (dep != null) {
        DependencyType depType = dep.getDependencyType();
        if (ctx_.isLogEnabled(MavenObtainer.class)) {
          OUT.println("Checking dependency " + pathBuilder.getFileName(depType));
        }
        repoDown_.findOrDownloadAndSha1(depType);
        finishedDepCount_.incrementAndGet();
        dep = deps_.poll();
      }
      if (finishedDepCount_.get() == depCount_.get()) {
        if (!finished_.get()) {
          finish();
        }
      }
    } catch (Exception x) {
      lastException_ = x;
    }
  }

  public void finish() {
    if (!finished_.get()) {
      try {
        semaphore_.acquire();
        if (!finished_.get()) {
          if (ctx_.isLogEnabled(MavenObtainer.class)) {
            OUT.println("Finished all MavenObtainer downloads.");
          }
          finished_.set(true);
        }
        semaphore_.release();
      } catch (InterruptedException x) {
        //do nothing this thread didn't get the acquire
      }
    } 
  }

  public void finishFindingDeps() {
    
    if (!finishedProjects_.get()) {
      try {
        semaphore4Deps_.acquire();
        if (ctx_.isLogEnabled(MavenObtainer.class)) {
          OUT.println("Finished finding dependencies for projects in " + projectsPath_ + 
              System.lineSeparator() + " there are " + uniqueDeps_.size());
        }
        finishedProjects_.set(true);
        deps_.addAll(uniqueDeps_);
        depCount_.set(uniqueDeps_.size());
        semaphore4Deps_.release();
      } catch (InterruptedException x) {
        while (!finishedProjects_.get()) {
          try {
            Thread.sleep(250);
          } catch (InterruptedException g) {
            //do nothing, continue execution
          }
        }
      }
        
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
