package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectsType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StagesAndProjectsType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependenciesType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependencyType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
public class LoadAndCleanProjects extends OldBaseConcurrentStage implements I_FabStage {
  public static final String COULD_NOT_FIND_THE_FILE = "Could not find the file ";

  private boolean run_ = true;
  private ConcurrentLinkedQueue<String> projectsQueue_ = new ConcurrentLinkedQueue<String>();
  private int projectsCount_;
  private final ConcurrentHashMap<String, NamedProject> projectsMemory_ = new ConcurrentHashMap<String, NamedProject>();
  private final Semaphore semaphore_ = new Semaphore(1);
  
  
  public LoadAndCleanProjects() {}
  
  public void setup(I_RunContext ctx) {
    super.setup(ctx);
    FabRunType type =  ctx.getRunType();
    switch (type) {
      case PROJECT:
        FabricateProjectType fpt = ctx.getProject();
        String projectPath = ctx.getProjectPath();
        File file = new File(projectPath);
        String projectName = file.getName();
        projectsMemory_.put(projectName, new NamedProject(projectName, fpt));
        calcProjectDependencyOrder(ctx_);
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
        if (log_.isLogEnabled(LoadAndCleanProjects.class)) {
          log_.println("There are " + pts.size() + " projects.");
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
        runProject(project);
        
        project = projectsQueue_.poll();
      }
      if (projectsCount_ != projectsMemory_.size()) {
        try {
          Thread.sleep(250);
        } catch (InterruptedException x) {
          //do nothing
        }
      }
      finish();
    } catch (Exception x) {
      super.finish(x);
    }
  }

  public void runProject(String project) throws FileNotFoundException, IOException {
    String filePath = projectsPath_ + File.separator + project + File.separator +
        "project.xml";
    File projectXmlFile = new File(filePath);
    if (!projectXmlFile.exists()) {
      throw new FileNotFoundException(COULD_NOT_FIND_THE_FILE + 
          System.lineSeparator() + filePath);
    }
    if (log_.isLogEnabled(LoadAndCleanProjects.class)) {
      log_.println("Reading " + filePath);
    }
    FabricateProjectType fabProject = xmlFiles_.parseProject_v1_0(filePath);
    projectsMemory_.put(project, new NamedProject(project, fabProject));
    cleanBuildForProjectRun(projectsPath_ + File.separator + project);
  }

  public void checkProjectDependency(String name, Set<String> projects, String projectName) {
    if (!projects.contains(projectName)) {
      throw new IllegalStateException("The following project;" + 
          System.lineSeparator() +
          name + 
          " references a project;" +
          System.lineSeparator() +
          projectName + 
          System.lineSeparator() +
          " which is NOT referenced in fabricate.xml");
    }
    if (name.equals(projectName)) {
      throw new IllegalStateException("The following project may NOT depend on itself;" + 
          System.lineSeparator() + name);
    }
  }
  
  public void finish() {
    try {
      semaphore_.acquire();
      calcProjectDependencyOrder(ctx_);
      if (log_.isLogEnabled(LoadAndCleanProjects.class)) {
        log_.println("LoadAndCleanProjects is finished.");
      }
      super.finish();
      semaphore_.release();
    } catch (InterruptedException x) {
      //do nothing, this thread didn't get to execute finish
    }
  }


  private void cleanBuildForProjectRun(String projectPath) {
    String buildPath = projectPath + File.separator + "build";
    
    File buildDir = new File(buildPath);
    if (buildDir.exists()) {
      try {
        files_.deleteRecursive(buildPath);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (!buildDir.mkdirs()) {
      throw new RuntimeException("There was a problem creating " + buildPath);
    }
  }
  
  
  /**
   * Note project dependency order is the order 
   * of projects based on their project dependency.
   * It doesn't look at external dependencies.
   * @param ctx_
   * @throws IllegalStateException
   */
  private void calcProjectDependencyOrder(I_RunContext ctx_) throws IllegalStateException {
    if (log_.isLogEnabled(LoadAndCleanProjects.class)) {
      log_.println("Calculating project dependency order for " + 
          projectsMemory_.size() + " projects.");
    }
    if (projectsMemory_.size() == 1) {
      //only one project assume everything else is already done for
      //it, and if not error out when it finds something that needs to be completed
      //ie a missing .jar file
      List<NamedProject> list = Collections.unmodifiableList(
          new ArrayList<NamedProject>(projectsMemory_.values()));
      ctx_.putInMemory("projectDependencyOrder", list);
      return;
    }
    List<String> order = new ArrayList<String>();
    Iterator<NamedProject> oit =  projectsMemory_.values().iterator();
    
    while (oit.hasNext()) {
      NamedProject proj = oit.next();
      String name = proj.getName();
      FabricateProjectType fp = proj.getProject();
      ProjectDependenciesType dt =  fp.getDependencies();
      int count = 0;
      if (dt == null) {
        order.add(name);
      } else {
        List<ProjectDependencyType> projects =  dt.getProject();
        if (projects == null) {
          order.add(name);
        } else if (projects.size() == 0){
          order.add(name);
        } else {
          count = projects.size();
        }
      }
      if (log_.isLogEnabled(LoadAndCleanProjects.class)) {
        if (order.contains(name)) {
          log_.println("The following project had NO project dependencies " + name);
        } else {
          log_.println("The following project had " + count + 
              " project dependencies " + name);
        }
      }
    }
    
    if (order.size() == 0) {
      throw new IllegalStateException("All projects depend on other projects,"
          + "which is a indication of circular project dependency.");
    }
    
    List<NamedProject> toDo = new ArrayList<NamedProject>(projectsMemory_.values());
    Iterator<NamedProject> it = toDo.iterator();
    while (it.hasNext()) {
      NamedProject np = it.next();
      if (order.contains(np.getName())) {
        it.remove();
      }
    }
    int lastSize = toDo.size();
    while (lastSize > 0){
      it = toDo.iterator();
      while (it.hasNext()) {
        NamedProject proj = it.next();
        String name = proj.getName();
        if (!order.contains(name)) {
          FabricateProjectType fp = proj.getProject();
          ProjectDependenciesType dt =  fp.getDependencies();
          List<ProjectDependencyType> projectDeps =  dt.getProject();
          List<String> projects = new ArrayList<String>();
          for (ProjectDependencyType pdt: projectDeps) {
            String projectName = pdt.getValue();
            checkProjectDependency(name, projectsMemory_.keySet(), projectName);
            projects.add(pdt.getValue());
          }
          if (order.containsAll(projects)) {
              order.add(name);
              it.remove();
          }
        }
      }
      int nextSize = toDo.size();
      if (lastSize == nextSize) {
        StringBuilder sb = new StringBuilder();
        it = toDo.iterator();
        while (it.hasNext()) {
          NamedProject np = it.next();
          sb.append(np.getName() + System.lineSeparator());
        }
        throw new IllegalStateException("Unable to find project dependency order"
            + " for the following projects."  + System.lineSeparator() + 
            sb.toString());
      }
      lastSize = nextSize;
    }
    List<NamedProject> projectOrder = new ArrayList<NamedProject>();
    for (String projectName: order) {
      NamedProject np = projectsMemory_.get(projectName);
      projectOrder.add(np);
    }
    List<NamedProject> list = Collections.unmodifiableList(projectOrder);
    ctx_.putInMemory(DefaultMemoryConstants.PROJECTS_MEMORY, projectsMemory_);
    ctx_.putInMemory(DefaultMemoryConstants.PROJECTS_DEPENDENCY_ORDER, list);
  }

  
}
