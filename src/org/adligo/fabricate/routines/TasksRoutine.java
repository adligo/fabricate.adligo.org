package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TasksRoutine extends AbstractRoutine implements I_FabricateAware {
  private static final Set<I_ExpectedRoutineInterface> EMPTY_SET = Collections.emptySet();
  protected List<TaskContext> tasks_ = new ArrayList<TaskContext>();
  protected I_Fabricate fabricate_;
  

  @Override
  public I_Fabricate getFabricate() {
    return fabricate_;
  }

  @Override
  public void setFabricate(I_Fabricate fab) {
    fabricate_ = fab;
  }
  
  @Override
  public boolean setup(I_FabricationMemoryMutant memory, I_RoutineMemoryMutant routineMemory)
      throws FabricationRoutineCreationException {
    setup();
    return super.setup(memory, routineMemory);
  }

  @Override
  public void setup(I_FabricationMemory memory, I_RoutineMemory routineMemory)
      throws FabricationRoutineCreationException {
    setup();
    super.setup(memory, routineMemory);
  }
  
  private void setup() throws FabricationRoutineCreationException {
    List<I_RoutineBrief> tasks = brief_.getNestedRoutines();
    
    for (I_RoutineBrief task: tasks) {
      String name = task.getName();
      I_FabricationRoutine routine =  taskFactory_.createRoutine(name, EMPTY_SET);
      routine.setSystem(system_);
      routine.setLocations(locations_);
      if (I_Fabricate.class.isAssignableFrom(routine.getClass())) {
        ((I_FabricateAware) routine).setFabricate(fabricate_);
      }
      tasks_.add(new TaskContext(routine, task));
    }
  }

}
