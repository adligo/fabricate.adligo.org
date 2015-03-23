package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_TaskProcessor;
import org.adligo.fabricate.routines.RoutineExecutionEngine;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FacetExecutor {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final RoutineFabricateFactory factory_;
  
  public FacetExecutor(I_FabSystem system, RoutineFabricateFactory factory) {
    factory_ = factory;
    log_ = system.getLog();
    system_ = system;
  }
  
  public FailureType run(final String facetName, final I_FacetSetup setup, FabricationMemoryMutant<Object> memory) {
    I_RoutineBuilder routineBuilder = new I_RoutineBuilder() {
      
      @Override
      public I_FabricationRoutine buildInitial(I_FabricationMemoryMutant<Object> memory, 
          I_RoutineMemoryMutant<Object> routineMemory)
          throws FabricationRoutineCreationException {
        I_FabricationRoutine toRet = setup.processFacetSetup(facetName);
        if (!toRet.setup(memory, routineMemory)) {
          return null;
        }
        return toRet;
      }

      @Override
      public I_FabricationRoutine build(I_FabricationMemory<Object> memory, 
          I_RoutineMemory<Object> routineMemory)
          throws FabricationRoutineCreationException {
        I_FabricationRoutine toRet = setup.processFacetSetup(facetName);
        toRet.setup(memory, routineMemory);
        return toRet;
      }
    };
    
    if (log_.isLogEnabled(FacetExecutor.class)) {
      log_.println(FacetExecutor.class.getName() + ".run(" + facetName + ")");
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
      result.setFacet(facetName);
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
      if (log_.isLogEnabled(FacetExecutor.class)) {
        log_.println(FacetExecutor.class.getName() + ".run(" + facetName + ") returns " + system_.lineSeparator() +
            result);
      }
      return result;
    }
    if (log_.isLogEnabled(FacetExecutor.class)) {
      log_.println(FacetExecutor.class.getName() + ".run(" + facetName + ") returns null");
    }
    return null;
  }
}
