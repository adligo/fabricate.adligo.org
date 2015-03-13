package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_ProjectBrief;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class provides a Queue of projects which can help implementers 
 * process projects concurrently in any order.
 * 
 * @diagram_sync on 1/26/2014 with Overview.seq
 * @author scott
 *
 */
public class ProjectBriefQueueRoutine extends TasksRoutine 
  implements I_ConcurrencyAware, I_FabricationRoutine, I_ProjectBriefsAware, I_ProjectProcessor {
  public static final String QUEUE = "queue";
  protected ConcurrentLinkedQueue<I_ProjectBrief> queue;
  private String currentTask_;
  private String currentProject_;
  
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(ProjectBriefQueueRoutine.class)) {
      log_.println(ProjectBriefQueueRoutine.class.getName() + " setup(I_FabricationMemoryMutant, I_RoutineMemoryMutant)");
    }
    queue = system_.newConcurrentLinkedQueue(I_ProjectBrief.class);
    List<I_ProjectBrief> briefs = fabricate_.getProjects();
    //fabricate_ is a Fabricate (immutable instance) and has filtered out null briefs;
    log_.println(ProjectBriefQueueRoutine.class.getName() + " has " +
        briefs.size() + " projects");
    queue.addAll(briefs);
    routineMemory.put(QUEUE, queue);
    return super.setup(memory, routineMemory);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    queue = (ConcurrentLinkedQueue<I_ProjectBrief>) routineMemory.get(QUEUE);
    super.setup(memory, routineMemory);
  }

  @Override
  public void run() {
    super.run();
    I_ProjectBrief project = queue.poll();
    if (log_.isLogEnabled(ProjectQueueRoutine.class)) {
      log_.println(ProjectQueueRoutine.class.getName() + system_.lineSeparator() +
          " project " + project);
    }
    while (project != null) {
      if (log_.isLogEnabled(ProjectQueueRoutine.class)) {
        log_.println(ProjectQueueRoutine.class.getName() + system_.lineSeparator() +
            " project " + project);
      }
      currentProject_ = project.getName();
      for (TaskContext task: tasks_) {
        
        I_FabricationRoutine taskRoutine = task.getTask();
        I_RoutineBrief brief = taskRoutine.getBrief();
        currentTask_ = brief.getName();
        if (I_ProjectBriefAware.class.isAssignableFrom(taskRoutine.getClass())) {
          ((I_ProjectBriefAware)  taskRoutine).setProjectBrief(project);
        }
        taskRoutine.run();
      }
      project = queue.poll();
    }
  }
  
  /**
   * allow sub classes to override 
   * this method for custom parameters.
   * @param taskRoutine
   */
  public void setupTask(I_FabricationRoutine taskRoutine) {}

  @Override
  public synchronized String getCurrentTask() {
    return currentTask_;
  }

  @Override
  public synchronized String getCurrentProject() {
    return currentProject_;
  }

}
