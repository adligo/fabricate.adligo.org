package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;

public class TaskContext {
  private final I_FabricationRoutine task_;
  private final I_RoutineBrief brief_;
  
  public TaskContext(I_FabricationRoutine task, I_RoutineBrief brief) {
    task_ = task;
    brief_ = brief;
  }

  /**
   * always resets the original brief from 
   * fabricate.xml and implicit routines,
   * each project can change the brief
   * after this method is called.
   * @return
   */
  public I_FabricationRoutine getTask() {
    task_.setBrief(brief_);
    return task_;
  }

  public I_RoutineBrief getBrief() {
    return brief_;
  }
}
