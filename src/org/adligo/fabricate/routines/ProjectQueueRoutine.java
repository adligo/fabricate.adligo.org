package org.adligo.fabricate.routines;

import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.ProjectBlock;
import org.adligo.fabricate.models.project.ProjectBlockKey;
import org.adligo.fabricate.routines.implicit.JarRoutine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class provides a Queue of projects which can help implementers 
 * process projects concurrently in any order.
 * 
 * @diagram_sync on 1/26/2014 with Overview.seq
 * @author scott
 *
 */
public abstract class ProjectQueueRoutine extends TasksRoutine implements 
  I_ConcurrencyAware {
  protected static final String PROJECTS_QUEUE = "projectsQueue";
  
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
  
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {

    //order the inital project queue
    projectsQueue_ = system_.newConcurrentLinkedQueue(I_Project.class);
    projectsQueue_.addAll(projects_);
    routineMemory.put(PROJECTS_QUEUE, projectsQueue_);
    if (log_.isLogEnabled(DependenciesQueueRoutine.class)) {
      log_.println(ProjectQueueRoutine.class.getSimpleName() + ".setup with " + projects_.size() + " participants");
    }

    return super.setupInitial(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    projectsQueue_ = (ConcurrentLinkedQueue<I_Project>) routineMemory.get(PROJECTS_QUEUE);
    
    super.setup(memory, routineMemory);
  }

  @Override
  public void run() {
    super.setRunning();
    
    I_Project project = projectsQueue_.poll();
    while (project != null) {
      
      I_RoutineBrief projectRoutineBrief = getProjectRoutine(project); 
      
      //do tasks
      Iterator<TaskContext> it = tasks_.iterator();
      while (it.hasNext()) {
        TaskContext task = it.next();
        I_FabricationRoutine taskRoutine = task.getTask();
        
        I_RoutineBrief brief = taskRoutine.getBrief();
        String currentTask = brief.getName();
        locationInfo_.setCurrentProject(currentTask);
        
        boolean run = true;
        if (I_ParticipationAware.class.isAssignableFrom(taskRoutine.getClass())) {
          run = false;
          I_RoutineBrief projectTaskRoutine = projectRoutineBrief.getNestedRoutine(currentTask);
          if (projectTaskRoutine != null) {
            run = true;
          }
        }
        if (run) {
          if (taskRoutine instanceof I_FabricateAware) {
            ((I_FabricateAware)  taskRoutine).setFabricate(fabricate_);
          }
          if (taskRoutine instanceof I_ProjectBriefAware) {
            ((I_ProjectBriefAware)  taskRoutine).setProjectBrief(project);
          }
          if (taskRoutine instanceof I_ProjectAware) {
            ((I_ProjectAware)  taskRoutine).setProject(project);
          }
          if (log_.isLogEnabled(ProjectQueueRoutine.class)) {
            log_.println(JarRoutine.class.getSimpleName() + "starting task '" + currentTask + "' "
                + "on project '" + project.getName() + "' locationInfo " + 
                locationInfo_.toString() +
                system_.lineSeparator() +
                this.toString()) ;
          }
          taskRoutine.run();
        }
      }
      
      project = projectsQueue_.poll();
    }
  }
  
  
}
