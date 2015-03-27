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
import org.adligo.fabricate.models.project.I_Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class TasksRoutine extends AbstractRoutine implements I_FabricateAware {
  private static final Set<I_ExpectedRoutineInterface> EMPTY_SET = Collections.emptySet();
  protected List<TaskContext> tasks_ = new ArrayList<TaskContext>();
  protected I_Fabricate fabricate_;
  private I_FabricationMemoryMutant<Object> memoryMutant_;
  private I_RoutineMemoryMutant<Object> routineMemoryMutant_;
  private I_FabricationMemory<Object> memory_;
  private I_RoutineMemory<Object> routineMemory_;
  
  @Override
  public I_Fabricate getFabricate() {
    return fabricate_;
  }

  public I_RoutineBrief getProjectRoutine(I_Project project) {
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
       * TODO
      projectRoutineBrief = p.getAr(name);
      */
    }
    return projectRoutineBrief;
  }
  
  @Override
  public void setFabricate(I_Fabricate fab) {
    fabricate_ = fab;
  }
  
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(TasksRoutine.class)) {
      log_.println(TasksRoutine.class.getName() + " setup(I_FabricationMemoryMutant, I_RoutineMemoryMutant)");
    }
    memoryMutant_ = memory;
    routineMemoryMutant_ = routineMemory;
    setup(true);
    return super.setupInitial(memory, routineMemory);
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    memory_ = memory;
    routineMemory_ = routineMemory;
    setup(false);
    super.setup(memory, routineMemory);
  }
  
  private void setup(boolean mutants) throws FabricationRoutineCreationException {
    List<I_RoutineBrief> tasks = brief_.getNestedRoutines();
    
    for (I_RoutineBrief task: tasks) {
      String name = task.getName();
      if (log_.isLogEnabled(TasksRoutine.class)) {
        log_.println(TasksRoutine.class.getName() + " setup(boolean) " + name + "/" + tasks.size());
      }
      
      I_FabricationRoutine routine =  taskFactory_.createRoutine(name, EMPTY_SET);
      if (log_.isLogEnabled(TasksRoutine.class)) {
        log_.println(TasksRoutine.class.getName() + " setup(boolean) " + routine);
      }
      routine.setSystem(system_);
      routine.setLocations(locations_);
      routine.setTraitFactory(traitFactory_);
      if (I_FabricateAware.class.isAssignableFrom(routine.getClass())) {
        ((I_FabricateAware) routine).setFabricate(fabricate_);
      }
      setupTask(routine);
      if (mutants) {
        routine.setupInitial(memoryMutant_, routineMemoryMutant_);
      } else {
        routine.setup(memory_, routineMemory_);
      }
      tasks_.add(new TaskContext(routine, task));
    }
  }

  /**
   * Allow sub classes to override this to pass in values
   * @param taskRoutine
   */
  public void setupTask(I_FabricationRoutine taskRoutine) {}

  @Override
  public void writeToMemory(I_FabricationMemoryMutant<Object> memory) {
    for (TaskContext tc: tasks_) {
      tc.getTask().writeToMemory(memory);
    }
    super.writeToMemory(memory);
  }

}
