package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.AlreadyLoggedException;
import org.adligo.fabricate.common.system.FailureTransport;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.I_FailureTransport;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineFabricateFactory;
import org.adligo.fabricate.routines.I_TaskProcessor;
import org.adligo.fabricate.routines.RoutineExecutionEngine;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This executes stages, 
 * this may be merged into a common routine execution class at some point.
 * @author scott
 *
 */
public class StageExecutor {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_RoutineFabricateFactory factory_;
  
  public StageExecutor(I_FabSystem system, I_RoutineFabricateFactory factory) {
    factory_ = factory;
    log_ = system.getLog();
    system_ = system;
  }
  
  public I_FailureTransport run(final String stageName, final I_StageSetup setup, FabricationMemoryMutant<Object> memory) {
    I_RoutineBuilder routineBuilder = new I_RoutineBuilder() {
      
      @Override
      public I_FabricationRoutine buildInitial(I_FabricationMemoryMutant<Object> memory, 
          I_RoutineMemoryMutant<Object> routineMemory)
          throws FabricationRoutineCreationException {
        if (log_.isLogEnabled(StageExecutor.class)) {
          log_.println(StageExecutor.class.getName() + ".buildInitial(memory, routineMemory)");
        }
        I_FabricationRoutine toRet = setup.processStageSetup(stageName);
        if (!toRet.setupInitial(memory, routineMemory)) {
          if (log_.isLogEnabled(StageExecutor.class)) {
            log_.println(StageExecutor.class.getName() + " buildInitial returning null");
          }
          return null;
        }
        if (log_.isLogEnabled(StageExecutor.class)) {
          log_.println(StageExecutor.class.getName() + " buildInitial returning " + stageName );
        }
        return toRet;
      }

      @Override
      public I_FabricationRoutine build(I_FabricationMemory<Object> memory, 
          I_RoutineMemory<Object> routineMemory)
          throws FabricationRoutineCreationException {
        I_FabricationRoutine toRet = setup.processStageSetup(stageName);
        toRet.setup(memory, routineMemory);
        return toRet;
      }

      @Override
      public void setNextRoutineName(String name) {
        
      }

      @Override
      public RoutineBriefOrigin getRoutineType() {
        // TODO Auto-generated method stub
        return null;
      }
    };
    
    if (log_.isLogEnabled(StageExecutor.class)) {
      log_.println(StageExecutor.class.getName() + ".run(" + stageName + ")");
    }
    Throwable failure = null;
    I_FabricationRoutine routine = null;
    try {
        RoutineExecutionEngine exe = factory_.createRoutineExecutionEngine(system_, routineBuilder);
        exe.runRoutines(memory);
        if (exe.hadFailure()) {
          failure = exe.getFailure();
          routine = exe.getRoutineThatFailed();
        }
    } catch (Throwable t) {
      failure = t;
    }
    if (failure != null) {
      FailureType result = new FailureType();
      result.setFacet(stageName);
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
      if (log_.isLogEnabled(StageExecutor.class)) {
        log_.println(StageExecutor.class.getName() + ".run(" + stageName + ") returns " + system_.lineSeparator() +
            result);
      }
      return new FailureTransport(failure instanceof AlreadyLoggedException, result);
    }
    if (log_.isLogEnabled(StageExecutor.class)) {
      log_.println(StageExecutor.class.getName() + ".run(" + stageName + ") returns null");
    }
    return null;
  }
}
