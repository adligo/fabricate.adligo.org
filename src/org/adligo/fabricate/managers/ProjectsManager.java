package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.I_CommandAware;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_TaskProcessor;
import org.adligo.fabricate.routines.RoutineExecutionEngine;
import org.adligo.fabricate.routines.implicit.ObtainTrait;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Set;

public class ProjectsManager {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  private FabricationMemoryMutant<Object> memory_;
  private final I_RoutineBuilder executorFactory_ = new I_RoutineBuilder() {
    

    @Override
    public I_FabricationRoutine build(I_FabricationMemoryMutant<Object> memory, 
        I_RoutineMemoryMutant<Object> routineMemory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processTraitSetup();
      if (!toRet.setup(memory, routineMemory)) {
        return null;
      }
      return toRet;
    }

    @Override
    public I_FabricationRoutine build(I_FabricationMemory<Object> memory, 
        I_RoutineMemory<Object> routineMemory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processTraitSetup();
      toRet.setup(memory, routineMemory);
      return toRet;
    }
  };
  
  public ProjectsManager(I_FabSystem system,  RoutineFabricateFactory factory) {
    system_ = system;
    log_ = system.getLog();
    factory_ = factory;
    fabricate_ = factory.getFabricate();
  }
  
  public FailureType setupAndRun(FabricationMemoryMutant<Object> memory) {
    memory_ = memory;
    FailureType failure = runObtain();
    if (failure != null) {
      return failure;
    }
    return null;
  }

  public FailureType runObtain() {
    if (log_.isLogEnabled(ProjectsManager.class)) {
      log_.println(ProjectsManager.class.getName() + " runObtain()");
    }
    Throwable failure = null;
    I_FabricationRoutine routine = null;
    try {
        RoutineExecutionEngine exe = factory_.createRoutineExecutionEngine(system_, executorFactory_);
        exe.runRoutines(memory_);
        if (exe.hadFailure()) {
          failure = exe.getFailure();
          routine = exe.getRoutineThatFailed();
        }
    } catch (Throwable t) {
      failure = t;
    }
    if (failure != null) {
      FailureType result = new FailureType();
      result.setStage(ObtainTrait.NAME);
      if (routine != null) {
        if (I_TaskProcessor.class.isAssignableFrom(routine.getClass())) {
          result.setTask(((I_TaskProcessor) routine).getCurrentTask());
        }
      }
      ByteArrayOutputStream baos = system_.newByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      ps.append(system_.lineSeparator());
      failure.printStackTrace(ps);
      String detail = baos.toString();
      result.setDetail(detail);
      return result;
    }
    return null;
  }
  
  private I_FabricationRoutine processTraitSetup() throws FabricationRoutineCreationException {
    Set<I_ExpectedRoutineInterface> es = Collections.emptySet();
    I_FabricationRoutine routine = factory_.createTrait(ObtainTrait.NAME,es);

    routine.setSystem(system_);
    if (I_FabricateAware.class.isAssignableFrom(routine.getClass())) {
      ((I_FabricateAware) routine).setFabricate(fabricate_);
    } 
    I_RoutineFactory traitFactory = factory_.getTraits();
    I_RoutineFactory taskFactory = traitFactory.createTaskFactory(ObtainTrait.NAME);
    routine.setTaskFactory(taskFactory);
    routine.setBrief(ObtainTrait.ROUTINE_BRIEF);
    routine.setTraitFactory(factory_.getTraits());
    if (I_CommandAware.class.isAssignableFrom(routine.getClass())) {
      I_RoutineFactory cmdFactory = factory_.getCommands();
      ((I_CommandAware) routine).setCommandFactory(cmdFactory);
    }
    return routine;
  }
}
