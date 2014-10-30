package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.shared.ProjectsMemory;
import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.external.files.FileUtils;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.ProjectType;
import org.adligo.fabricate.xml.io.ProjectsType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;
import org.adligo.fabricate.xml_io.ProjectIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * This class reads the project.xml files from disk
 * into memory and stores them for use later by other tasks
 * in the ProjectsMemory class.  In addition it deletes the 
 * ${project}/build directory and re creates it so that
 * other tasks have a common location for output.
 * 
 * 
 * This task is not participation aware, it doesn't check the project.xml file 
 * for the stage, it always executes.
 * @author scott
 *
 */
public class LoadAndCleanProjects extends BaseConcurrentStage implements I_FabStage {
  private boolean run_ = true;
  private ConcurrentLinkedQueue<String> projectsQueue_ = new ConcurrentLinkedQueue<String>();
  private int projectsCount_;
  private final Semaphore semaphore_ = new Semaphore(1);
  
  @Override
  public void setup(I_FabContext ctx) {
    super.setup(ctx);
    FabRunType type =  ctx.getRunType();
    switch (type) {
      case PROJECT:
        FabricateProjectType fpt = ctx.getProject();
        String projectPath = ctx.getProjectPath();
        File file = new File(projectPath);
        String projectName = file.getName();
        ProjectsMemory.add(new NamedProject(projectName, fpt));
        ProjectsMemory.calcProjectDependencyOrder(ctx_);
        cleanBuildForProjectRun(projectsPath_ + File.separator + projectName);
        //TODO modify the depot to remove it's entries(jars) for this project
        run_ = false;
        break;
      case AGGERGRATE:
        throw new RuntimeException("LoadAndCleanProjects does NOT support aggrregate runs.");
      default:
        FabricateType fabType = ctx.getFabricate();
        StagesAndProjectsType stagesAndProjects = fabType.getProjectGroup();
        ProjectsType projectsType = stagesAndProjects.getProjects();
        List<ProjectType> pts =  projectsType.getProject();
        if (ctx_.isLogEnabled(LoadAndCleanProjects.class)) {
          ThreadLocalPrintStream.println("There are " + pts.size() + " projects.");
        }
        
        projectsCount_  = pts.size();
        for (ProjectType proj: pts) {
          String name = proj.getName();
          projectsQueue_.add(name);
        }
    }
    
    
  }
  
  @Override
  public void run() {
    if (!run_) {
      return;
    }
    try {
      String project = projectsQueue_.poll();
      while (project != null) {
        String filePath = projectsPath_ + File.separator + project + File.separator +
            "project.xml";
        File projectXmlFile = new File(filePath);
        if (!projectXmlFile.exists()) {
          throw new FileNotFoundException("Could not find the file " + 
              System.lineSeparator() + filePath);
        }
        if (ctx_.isLogEnabled(LoadAndCleanProjects.class)) {
          ThreadLocalPrintStream.println("Reading " + filePath);
        }
        FabricateProjectType fabProject = ProjectIO.parse(projectXmlFile);
        ProjectsMemory.add(new NamedProject(project, fabProject));
        cleanBuildForProjectRun(projectsPath_ + File.separator + project);
        
        project = projectsQueue_.poll();
      }
      if (projectsCount_ != ProjectsMemory.size()) {
        try {
          Thread.sleep(250);
        } catch (InterruptedException x) {
          //do nothing
        }
      }
      finish();
    } catch (Exception x) {
      lastException_ = x;
    }
  }

  public void finish() {
    try {
      if (!finished_.get()) {
        semaphore_.acquire();
        if (!finished_.get()) {
          ProjectsMemory.calcProjectDependencyOrder(ctx_);
          if (ctx_.isLogEnabled(LoadAndCleanProjects.class)) {
            ThreadLocalPrintStream.println("LoadAndCleanProjects is finished.");
          }
          finished_.set(true);
        }
        semaphore_.release();
      }
    } catch (InterruptedException x) {
      //do nothing, this thread didn't get to execute finish
    }
  }


  private void cleanBuildForProjectRun(String projectPath) {
    String buildPath = projectPath + File.separator + "build";
    
    File buildDir = new File(buildPath);
    if (buildDir.exists()) {
      try {
        FileUtils fus = new FileUtils(ctx_);
        fus.removeRecursive(
            Paths.get(new File(buildPath).toURI()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (!buildDir.mkdirs()) {
      throw new RuntimeException("There was a problem creating " + buildPath);
    }
  }
}
