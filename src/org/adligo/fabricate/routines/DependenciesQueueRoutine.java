package org.adligo.fabricate.routines;

import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.ProjectBlock;
import org.adligo.fabricate.models.project.ProjectBlockKey;
import org.adligo.fabricate.models.project.ProjectDependencyOrderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class provides a Queue of projects which can help implementers 
 * process projects concurrently in project dependency order which is
 * fairly tricky, because routine instances must wait on other 
 * specific routine instances.
 * 
 * @diagram_sync on 1/26/2014 with Overview.seq
 * @author scott
 *
 */
public class DependenciesQueueRoutine extends TasksRoutine implements 
  I_ConcurrencyAware, I_FabricationRoutine, I_ProjectsAware {
  protected static final String PROJECTS_BLOCK_MAP = "projectsBlockMap";
  protected static final String PROJECTS_QUEUE = "projectsQueue";
  /**
   * key to a boolean in the routine memory
   */
  protected static final String SINGLE_PROJECT = "singleProject";
  
  protected I_Depot depot_;
  
  protected List<I_Project> projects_;
  protected ConcurrentLinkedQueue<I_Project> projectsQueue_;
  protected boolean singleProject_;
  
  // the key to this is the current threads projects name space
  // the blocking projects name the value is a ArrayBlockingQueue
  // with size 1 that the current thread adds to the concurrent hash
  // map, when a project finishes it spins through the map entries
  // looking for it's name and adds a item to the BlockingQueue. 
 protected ConcurrentHashMap<ProjectBlockKey, ProjectBlock> projectBlockMap_;
 
  public List<I_Project> getProjects() {
    return new ArrayList<I_Project>(projects_);
  }
  
  public void setProjects(Collection<I_Project> projects) {
    projects_ = new ArrayList<I_Project>(projects);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {

    //order the inital project queue
    projectsQueue_ = system_.newConcurrentLinkedQueue(I_Project.class);
    List<I_Project> participants = identifyParticipants(projects_); 
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(DependenciesQueueRoutine.class.getSimpleName() + ".setup ProjectDependencyOrderer");
    }
    List<I_Project> allProjects = (List<I_Project>) memory.get(FabricationMemoryConstants.LOADED_PROJECTS);
    ProjectDependencyOrderer orderer = new ProjectDependencyOrderer(allProjects, system_);
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(DependenciesQueueRoutine.class.getSimpleName() + ".setup orderer ");
    }
    
    List<I_Project> orderedProjects = new ArrayList<I_Project>(orderer.getProjects());
    Iterator<I_Project> opIt = orderedProjects.iterator();
    Set<String> cachedProjects = new HashSet<String>();
    while(opIt.hasNext()) {
      I_Project proj = opIt.next();
      if (!participants.contains(proj)) {
        cachedProjects.add(proj.getName());
        opIt.remove();
      }
    }
    projectsQueue_.addAll(orderedProjects);
    routineMemory.put(PROJECTS_QUEUE, projectsQueue_);
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(DependenciesQueueRoutine.class.getSimpleName() + ".setup with " + orderedProjects.size() + " participants");
    }
    
    projectBlockMap_ = system_.newConcurrentHashMap(ProjectBlockKey.class, ProjectBlock.class);
    
    // Pre fill the projectBlockMap, so that all projects are aware 
    // of the projects they depend on here.  This will ensure
    // that some sort of deadlock race condition doesn't occur ;
    // i.e. If it wasn't done here, projectA which depends on projectB
    // could put that it depended on B in the map, with out projectB
    // notifying it.
    if (participants.size() > 1) {
      singleProject_ = false;
      routineMemory.put(SINGLE_PROJECT, false);
      for (I_Project project: allProjects) {
        List<I_ProjectDependency> pdeps =  project.getProjectDependencies();
        for (I_ProjectDependency dep: pdeps) {
          String projectName = project.getName();
          String blockingProjectName = dep.getProjectName();
          ProjectBlock value = new ProjectBlock(projectName, blockingProjectName);
          if (cachedProjects.contains(blockingProjectName)) {
            try {
              value.setProjectFinished();
            } catch (InterruptedException e) {
              system_.currentThread().interrupt();
            }
          }
          projectBlockMap_.put(new ProjectBlockKey(projectName, blockingProjectName), 
              value);
        }
      }
      routineMemory.put(PROJECTS_BLOCK_MAP, projectBlockMap_);
      if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
        log_.println(DependenciesQueueRoutine.class.getSimpleName() + ".setup.super");
      }
    } else {
      singleProject_ = true;
      routineMemory.put(SINGLE_PROJECT, true);
    }
    return super.setup(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    projectBlockMap_ = (ConcurrentHashMap<ProjectBlockKey, ProjectBlock>) routineMemory.get(PROJECTS_BLOCK_MAP);
    projectsQueue_ = (ConcurrentLinkedQueue<I_Project>) routineMemory.get(PROJECTS_QUEUE);
    singleProject_ = (boolean) routineMemory.get(SINGLE_PROJECT);
    
    super.setup(memory, routineMemory);
  }


  private List<I_Project> identifyParticipants(Collection<I_Project> projects) {
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(DependenciesQueueRoutine.class.getSimpleName() + ".identifyParticipants " + projects.size() +
          " thread " + system_.currentThreadName());
    }
    
    boolean participationAware = this instanceof I_ParticipationAware;
    
    List<I_Project> copy = new ArrayList<I_Project>();
    //copy using a iterator, I think doing new ArrayList<I_Project>(projects)
    // in the above line was causing a deadlock?
    Iterator<I_Project> pits =  projects.iterator();
    while(pits.hasNext()) {
      copy.add(pits.next());
    }
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(DependenciesQueueRoutine.class.getSimpleName() + " copy has " + copy.size() + participationAware);
    }
    if (participationAware) {
      if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
        log_.println(DependenciesQueueRoutine.class.getSimpleName() + " participationAware 2");
      }
      
      String name = brief_.getName();
      if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
        log_.println(DependenciesQueueRoutine.class.getSimpleName() + " brief name is  " + name);
      }
      boolean command = brief_.isCommand();
      boolean stage = brief_.isStage();
      boolean archive = brief_.isArchivalStage();
      if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
        log_.println(DependenciesQueueRoutine.class.getSimpleName() + " command " +
            command + " stage " + stage + " archive " + archive);
      }
      
      Iterator<I_Project> it = copy.iterator();
      while (it.hasNext()) {
        I_Project p = it.next();
        if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
          log_.println(DependenciesQueueRoutine.class.getSimpleName() + ".checking " + p.getName());
        }
        if (command) {
          I_RoutineBrief cmd = p.getCommand(name);
          if (cmd == null) {
            it.remove();
          }
        } else if (stage) {
          I_RoutineBrief stageBrief = p.getStage(name);
          if (stageBrief == null) {
            it.remove();
          }
        } else if (archive) {
          /**
           * TODO
          I_RoutineBrief stageBrief = p.getAr(name);
          if (stageBrief == null) {
            it.remove();
          }
          */
        }
      }
    } 
    return copy;
  }

  @Override
  public void run() {
    setRunning();
    I_Project project =  projectsQueue_.poll();
    while (project != null) {
      if (!singleProject_) {
        waitForProjectsDependedOnToFinish(project);
      }
      String name = brief_.getName();
      boolean command = brief_.isCommand();
      boolean stage = brief_.isStage();
      boolean archive = brief_.isArchivalStage();
      I_RoutineBrief projectRoutineBrief = null; 
      
      if (command) {
        projectRoutineBrief = project.getCommand(name);
      } else if (stage) {
        projectRoutineBrief = project.getStage(name);
      } else if (archive) {
        /**
        projectRoutineBrief = p.getAr(name);
        */
      }
      //do tasks
      for (TaskContext task: tasks_) {
        
        I_FabricationRoutine taskRoutine = task.getTask();
        
        I_RoutineBrief brief = taskRoutine.getBrief();
        String currentTask = brief.getName();
        locationInfo_.setCurrentTask(currentTask);
        
        boolean run = true;
        if (I_ParticipationAware.class.isAssignableFrom(taskRoutine.getClass())) {
          run = false;
          I_RoutineBrief projectTaskRoutine = projectRoutineBrief.getNestedRoutine(currentTask);
          if (projectTaskRoutine != null) {
            run = true;
          }
        }
        if (run) {
          if (I_ProjectBriefAware.class.isAssignableFrom(taskRoutine.getClass())) {
            ((I_ProjectBriefAware)  taskRoutine).setProjectBrief(project);
          }
          if (I_ProjectAware.class.isAssignableFrom(taskRoutine.getClass())) {
            ((I_ProjectAware)  taskRoutine).setProject(project);
          }
          taskRoutine.run();
        }
      }
      notifyProjectFinished(project);
      project =  projectsQueue_.poll();
    }
  }

  protected void notifyProjectFinished(I_Project project) {
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println("notifying that " + project + " is finished.");
    }
    String projectName = project.getName();
    
    Collection<ProjectBlock> blocks = projectBlockMap_.values();
    for (ProjectBlock block: blocks) {
      if (projectName.equals(block.getBlockingProject())) {
        try {
          block.setProjectFinished();
        } catch (InterruptedException x) {
          system_.currentThread().interrupt();
        }
      }
    }
  }
  
  protected void waitForProjectsDependedOnToFinish(I_Project project) {
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println("waiting for projects depended on by " + project + " to finish.");
    }
    locationInfo_.setWaitingProject(project);
    List<I_ProjectDependency> pdeps =  project.getProjectDependencies();
    boolean waiting = true;
    while (waiting) {
      int countDone = 0;
      for (I_ProjectDependency dep: pdeps) {
        String projectName = project.getName();
        String blockingProjectName = dep.getProjectName();
        ProjectBlock pb = projectBlockMap_.get(new ProjectBlockKey(projectName, blockingProjectName));
        try {
          if (pb.waitUntilUnblocked(1000)) {
            countDone++;
          }
        } catch (InterruptedException x) {
          system_.currentThread().interrupt();
        }
      }
      if (countDone == pdeps.size()) {
        waiting = false;
      }
    }
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(project + " is done waiting.");
    }
    locationInfo_.setWaitingProject(null);
  }

  @Override
  public String getCurrentLocation() {
    I_Project project = locationInfo_.getWaitingProject();
    if (project != null) {
      RoutineBriefOrigin origin = brief_.getOrigin();
      String name = brief_.getName();
      String projectName = project.getName();
      
      switch (origin) {
        case ARCHIVE_STAGE:
        case FABRICATE_ARCHIVE_STAGE:
        case IMPLICIT_ARCHIVE_STAGE:
        case PROJECT_ARCHIVE_STAGE:
          String archMessage = sysMessages_.getArchiveStageXProjectYIsWaitingOnTheFollowingProjects();
          return returnLocation(project, name, projectName, archMessage);
        case IMPLICIT_STAGE:
        case FABRICATE_STAGE:
        case PROJECT_STAGE:
        case STAGE:
          String stageMessage = sysMessages_.getBuildStageXProjectYIsWaitingOnTheFollowingProjects();
          return returnLocation(project, name, projectName, stageMessage);
        case IMPLICIT_COMMAND:
        case FABRICATE_COMMAND:
        case PROJECT_COMMAND:
        case COMMAND:
          String cmdMessage = sysMessages_.getCommandXProjectYIsWaitingOnTheFollowingProjects();
          return returnLocation(project, name, projectName, cmdMessage);
        case IMPLICIT_FACET:
        case FABRICATE_FACET:
        case FACET:
          String facetMessage = sysMessages_.getFacetXIProjectYIsWaitingOnTheFollowingProjects();
          return returnLocation(project, name, projectName, facetMessage);
      }
    }
    return super.getCurrentLocation();
  }

  protected String returnLocation(I_Project project, String name, String projectName, String message) {
    message = message.replace("<X/>", name);
    message = message.replace("<Y/>", projectName);
    return message + system_.lineSeparator() + getProjectsWaitingOn(project);
  }
  
  private String getProjectsWaitingOn(I_Project project) {
    StringBuilder toRet = new StringBuilder();
    boolean first = true;
    List<I_ProjectDependency> pdeps =  project.getProjectDependencies();
   
    for (I_ProjectDependency dep: pdeps) {
      String projectName = project.getName();
      String blockingProjectName = dep.getProjectName();
      ProjectBlock pb = projectBlockMap_.get(new ProjectBlockKey(projectName, blockingProjectName));
      if (pb.isBlocking()) {
        if (first) {
          first = false;
        } else {
          toRet.append(system_.lineSeparator());
        }
        toRet.append(blockingProjectName);
      }
    }
    return toRet.toString();
  }
}
