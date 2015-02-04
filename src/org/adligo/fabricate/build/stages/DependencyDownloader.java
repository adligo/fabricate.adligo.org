package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.DependenciesManager;
import org.adligo.fabricate.repository.RepositoryManager;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependenciesType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.io.IOException;
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
 * @deprecated
 * @see DependenciesManager
 * @author scott
 *
 */
public class DependencyDownloader extends OldBaseConcurrentStage implements I_FabStage {
   private ConcurrentLinkedQueue<NamedProject> projects_;
  private ConcurrentLinkedQueue<Dependency> deps_ = new ConcurrentLinkedQueue<Dependency>();
  private CopyOnWriteArraySet<String> libsDone_ = new CopyOnWriteArraySet<String>();
  private CopyOnWriteArraySet<Dependency> uniqueDeps_ = new CopyOnWriteArraySet<Dependency>();
  

  private int projectCount_;
  private AtomicInteger finishedProjectCount_ = new AtomicInteger(0);
  private AtomicInteger depCount_ = new AtomicInteger(0);
  private AtomicInteger finishedDepCount_ = new AtomicInteger(0);

  private AtomicBoolean finishedProjects_ = new AtomicBoolean(false);
  private RepositoryManager repoDown_;
  private final Semaphore semaphore4Deps_ = new Semaphore(1);
  private final Semaphore semaphore_ = new Semaphore(1);
  
  
  public DependencyDownloader(I_FabSystem sys) {
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
  }
  
  @Override
  public void setup(I_RunContext ctx) {
    super.setup(ctx);
    FabricateType fab = ctx_.getFabricate();
    FabricateDependencies fabDeps = fab.getDependencies();
    List<String> remoteRepos = fabDeps.getRemoteRepository();
    //TODO system should be passed
    repoDown_ = new RepositoryManager(new FabSystem(), ctx_.getLocalRepositoryPath());
    repoDown_.setPathBuilder(new DefaultRepositoryPathBuilder(ctx_.getLocalRepositoryPath(), 
        File.separator));
    repoDown_.setRepositories(remoteRepos);
    
    projectsPath_ = ctx_.getProjectsPath();
    if (log_.isLogEnabled(DependencyDownloader.class)) {
      log_.println("Finding dependencies for projects in " + projectsPath_);
    }
    
    projects_ = new ConcurrentLinkedQueue<NamedProject>((List<NamedProject>)
        ctx_.getFromMemory(DefaultMemoryConstants.PROJECTS_DEPENDENCY_ORDER));
    projectCount_ = projects_.size();
    finishedProjectCount_.set(0);
  }

  @Override
  public void run() {
    try {
      NamedProject project = projects_.poll();
      
      String fabricateDir = ctx_.getFabricateDirPath();
      while (project != null) {
        String projectName = project.getName();
        
        FabricateProjectType fabProject = project.getProject();
      
        DependenciesType deps = fabProject.getDependencies();
        if (deps != null) {
          List<LibraryReferenceType> libs =  deps.getLibrary();
          if (libs != null) {
            for (LibraryReferenceType lib: libs) {
              String libName = lib.getValue();
              if (log_.isLogEnabled(DependencyDownloader.class)) {
                log_.println("project " + projectName + " has library " + libName);
              }
              addLibrary(fabricateDir, libName);
            }
          }
          List<DependencyType> depsList = deps.getDependency();
          for (DependencyType dep: depsList) {
            uniqueDeps_.add(new Dependency(dep));
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
      DefaultRepositoryPathBuilder pathBuilder = new DefaultRepositoryPathBuilder("", "");
      Dependency dep = deps_.poll();
      while (dep != null) {
        if (log_.isLogEnabled(DependencyDownloader.class)) {
          log_.println("Checking dependency " + pathBuilder.getArtifactFileName(dep));
        }
        String type = dep.getType();
        if (type == null || !"ide".equalsIgnoreCase(type)) {
          repoDown_.findOrDownloadAndVerify(dep);
        }
        finishedDepCount_.incrementAndGet();
        dep = deps_.poll();
      }
      if (finishedProjects_.get()) {
        if (finishedDepCount_.get() == depCount_.get()) {
          finish();
        }
      }
    } catch (Exception x) {
      super.finish(x);
    }
  }

  public void finish() {
   
      try {
        semaphore_.acquire();
        if (log_.isLogEnabled(DependencyDownloader.class)) {
          log_.println("Finished all MavenObtainer downloads.");
        }
        super.finish();
        semaphore_.release();
      } catch (InterruptedException x) {
        //do nothing this thread didn't get the acquire
      }
  }

  public void finishFindingDeps() {
    
    if (!finishedProjects_.get()) {
      try {
        semaphore4Deps_.acquire();
        if (log_.isLogEnabled(DependencyDownloader.class)) {
          log_.println("Finished finding dependencies for projects in " + projectsPath_ + 
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
    LibraryType libDeps = xmlFiles_.parseLibrary_v1_0(fabricateDir + 
        File.separator + "lib" + File.separator + lib + ".xml");
    DependenciesType depsType = libDeps.getDependencies();
    
    if (depsType != null) {
    List<LibraryReferenceType> subLibs = depsType.getLibrary();
      for (LibraryReferenceType subLib: subLibs) {
        //recurse
        addLibrary(fabricateDir, subLib.getValue());
      }
      List<DependencyType> deps = depsType.getDependency();
      for (DependencyType dep: deps) {
        uniqueDeps_.add(new Dependency(dep));
      }
    }
  }

}
