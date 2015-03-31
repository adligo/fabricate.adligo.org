package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.MemoryLock;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.project.Project;
import org.adligo.fabricate.models.project.ProjectMutant;
import org.adligo.fabricate.repository.LibraryResolver;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependenciesType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TODO this class will load the project data (project.xml directory etc) into memory
 * for later usage.
 * 
 * @author scott
 *
 */
public class LoadProjectTask extends ProjectBriefAwareRoutine {
  public static final String DEPENDENCIES_FILTER = LoadProjectTask.class.getName() + ".dependenciesFilter";
  private DependenciesFilter dependenciesFilter_;
  
  public static final String PROJECTS_QUEUE = LoadProjectTask.class.getName() + ".projects";
  private ConcurrentLinkedQueue<Project> projects_;
  
  
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
  
    dependenciesFilter_ = new DependenciesFilter();
    routineMemory.put(DEPENDENCIES_FILTER, dependenciesFilter_);
    
    projects_ = system_.newConcurrentLinkedQueue(Project.class);
    routineMemory.put(PROJECTS_QUEUE, projects_);
    return super.setupInitial(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    dependenciesFilter_ = (DependenciesFilter) routineMemory.get(DEPENDENCIES_FILTER);
    projects_ = (ConcurrentLinkedQueue<Project>) routineMemory.get(PROJECTS_QUEUE);
    
    super.setup(memory, routineMemory);
  }


  @Override
  public void run() {
    
    String projectsDir = fabricate_.getProjectsDir();
    String projectName = brief_.getName();
    
    String projectDir = projectsDir + projectName + files_.getNameSeparator();
    String projectFile = projectDir +  "project.xml";
    if (log_.isLogEnabled(LoadProjectTask.class)) {
      log_.println("parseing project '" + projectName + "' " + system_.lineSeparator() +
          projectFile);
    }
    
    FabricateProjectType project = null;
    try {
      project = xmlFiles_.parseProject_v1_0(projectFile);
    } catch (IOException e) {
      //pass to run monitor
      throw new RuntimeException(e);
    }
    if (log_.isLogEnabled(LoadProjectTask.class)) {
      log_.println("parsed project '" + projectName + "' ");
    }
    List<I_Dependency> libDeps = null;
    LibraryResolver resolver = new LibraryResolver(system_, fabricate_);
    ProjectDependenciesType pdeps =  project.getDependencies();
    if (pdeps != null) {
      List<LibraryReferenceType> libs  = pdeps.getLibrary();
      libDeps = resolver.getDependencies(libs, projectName);
    }
    try {
      ProjectMutant pm = new ProjectMutant(projectDir, brief_, project);
      List<I_Dependency> normDeps = new ArrayList<I_Dependency>();
      if (libDeps != null && libDeps.size() >= 1) {
        normDeps.addAll(libDeps);
      }
      List<I_Dependency> deps = pm.getDependencies();
      if (deps != null && deps.size() >= 1) {
        normDeps.addAll(deps);
      }
      if (normDeps.size() >= 1) {
        pm.setNormalizedDependencies(normDeps);
        dependenciesFilter_.add(normDeps);
      }
      if (log_.isLogEnabled(LoadProjectTask.class)) {
        StringBuilder sb = new StringBuilder();
        sb.append("project '" + pm.getName() + "' has " + normDeps.size() + " external dependencies.");
        for (I_Dependency dep: normDeps) {
          sb.append(system_.lineSeparator() + "\t" + dep.toString());
        }
        log_.println(sb.toString());
      }
      Project p = new Project(pm);
      projects_.add(p);
    } catch (ClassNotFoundException e) {
      //pass to run monitor
      throw new RuntimeException(e);
    }
    String buildDir = projectDir + files_.getNameSeparator() + "build";
    if (files_.exists(buildDir)) {
      try {
        files_.deleteRecursive(buildDir);
      } catch (IOException e) {
        //pass to run monitor
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public synchronized void writeToMemory(I_FabricationMemoryMutant<Object> memory) {
    List<Project> ps = new ArrayList<Project>();
    ps.addAll(projects_);
    List<Project> projects = Collections.unmodifiableList(ps);
    memory.put(FabricationMemoryConstants.PROJECTS_LOADED, projects);
    memory.addLock(new MemoryLock(FabricationMemoryConstants.PROJECTS_LOADED, 
              Collections.singletonList(LoadProjectTask.class.getName())));
    
    Map<String, Project> projectsMap = new HashMap<String,Project>();
    if (log_.isLogEnabled(LoadProjectTask.class)) {
      log_.println("LoadProjectTask loaded " + projects_.size() + " projects.");
    }
    for (Project project: projects_) {
      projectsMap.put(project.getName(), project);
    }
    if (log_.isLogEnabled(LoadProjectTask.class)) {
      log_.println("LoadProjectTask loaded " + projectsMap.size() + " projects.");
    }
    projectsMap = Collections.unmodifiableMap(projectsMap);
    memory.put(FabricationMemoryConstants.PROJECTS_LOADED_MAP, projectsMap);
    memory.addLock(new MemoryLock(FabricationMemoryConstants.PROJECTS_LOADED_MAP, 
              Collections.singletonList(LoadProjectTask.class.getName())));
    
    
    List<I_Dependency> deps =  dependenciesFilter_.get();
    if (log_.isLogEnabled(LoadProjectTask.class)) {
      log_.println("LoadProjectTask loaded " + deps.size() + " dependencies.");
    }
    memory.put(FabricationMemoryConstants.DEPENDENCIES, deps);
    memory.addLock(new MemoryLock(FabricationMemoryConstants.DEPENDENCIES, 
        Collections.singletonList(LoadProjectTask.class.getName())));
    
    
    super.writeToMemory(memory);
  }


}
