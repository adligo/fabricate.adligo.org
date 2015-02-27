package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_ProjectBrief;

import java.util.ArrayList;
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
  implements I_ConcurrencyAware, I_FabricationRoutine, I_ProjectBriefsAware {
  public static final String QUEUE = "queue";
  private List<String> projectsToSkip_ = new ArrayList<String>();
  protected ConcurrentLinkedQueue<I_ProjectBrief> queue;
  

  @Override
  public boolean setup(I_FabricationMemoryMutant memory, I_RoutineMemoryMutant routineMemory)
      throws FabricationRoutineCreationException {
    queue = new ConcurrentLinkedQueue<I_ProjectBrief>();
    List<I_ProjectBrief> briefs = fabricate_.getProjects();
    for (I_ProjectBrief brief: briefs) {
      if (!projectsToSkip_.contains(brief.getName())) {
        queue.add(brief);
      }
    }
    routineMemory.put(QUEUE, queue);
    return super.setup(memory, routineMemory);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory memory, I_RoutineMemory routineMemory)
      throws FabricationRoutineCreationException {
    
    queue = (ConcurrentLinkedQueue<I_ProjectBrief>) routineMemory.get(QUEUE);
    super.setup(memory, routineMemory);
  }

  @Override
  public void run() {
    super.run();
    I_ProjectBrief project = queue.poll();
    while (project != null) {
      for (TaskContext task: tasks_) {
        I_FabricationRoutine taskRoutine = task.getTask();
        if (I_ProjectBriefAware.class.isAssignableFrom(taskRoutine.getClass())) {
          ((I_ProjectBriefAware)  taskRoutine).setProjectBrief(project);
        }
        setupTask(taskRoutine);
        taskRoutine.run();
      }
    }
  }
  
  /**
   * allow sub classes to override 
   * this method for custom parameters.
   * @param taskRoutine
   */
  public void setupTask(I_FabricationRoutine taskRoutine) {}

  @Override
  public List<String> getProjectsToSkip() {
    return projectsToSkip_;
  }

  @Override
  public void setProjectsToSkip(List<String> projectNames) {
    projectsToSkip_.addAll(projectNames);
    projectsToSkip_.remove(null);
  }
}
