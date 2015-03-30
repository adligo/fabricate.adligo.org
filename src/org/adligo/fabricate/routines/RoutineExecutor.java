package org.adligo.fabricate.routines;

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
 * This executes facets, 
 * this may be merged into a common routine execution class at some point.
 * @author scott
 *
 */
public class RoutineExecutor implements I_RoutineExecutor {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_RoutineFabricateFactory factory_;
  
  public RoutineExecutor(I_FabSystem system, I_RoutineFabricateFactory factory) {
    factory_ = factory;
    log_ = system.getLog();
    system_ = system;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutineExecutor#run(java.lang.String, org.adligo.fabricate.routines.I_RoutineBuilder, org.adligo.fabricate.models.common.FabricationMemoryMutant)
   */
  @Override
  @SuppressWarnings("incomplete-switch")
  public I_FailureTransport run(final String routineName, final I_RoutineBuilder builder, FabricationMemoryMutant<Object> memory) {
    
    if (log_.isLogEnabled(RoutineExecutor.class)) {
      log_.println(RoutineExecutor.class.getName() + ".run(" + routineName + ")");
    }
    Throwable failure = null;
    I_FabricationRoutine routine = null;
    try {
        builder.setNextRoutineName(routineName);
        RoutineExecutionEngine exe = factory_.createRoutineExecutionEngine(system_, builder);
        exe.runRoutines(memory);
        if (log_.isLogEnabled(RoutineExecutor.class)) {
          log_.println(RoutineExecutor.class.getName() + ".run(" + routineName + ") had failure " +
              exe.hadFailure());
        }
        if (exe.hadFailure()) {
          failure = exe.getFailure();
          if (log_.isLogEnabled(RoutineExecutor.class)) {
            log_.println(RoutineExecutor.class.getName() + ".run(" + routineName + ") failure is " + failure);
          }
          routine = exe.getRoutineThatFailed();
        }
    } catch (Throwable t) {
      if (log_.isLogEnabled(RoutineExecutor.class)) {
        log_.println(RoutineExecutor.class.getName() + ".run(" + routineName + ") caught exception.");
      }
      failure = t;
    }
    if (failure != null) {
      FailureType result = new FailureType();
      switch(builder.getRoutineType()) {
        case ARCHIVE_STAGE:
         result.setArchiveStage(routineName);
         break;
        case COMMAND:
          result.setCommand(routineName);
          break;
        case FACET:
          result.setFacet(routineName);
          break;
        case STAGE:
          result.setStage(routineName);
          break;
        case TRAIT:
          result.setTrait(routineName);
          break;
      }
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
      if (log_.isLogEnabled(RoutineExecutor.class)) {
        log_.println(RoutineExecutor.class.getName() + ".run(" + routineName + ") returns " + system_.lineSeparator() +
            result);
      }
      return new FailureTransport(failure instanceof AlreadyLoggedException, result);
    }
    if (log_.isLogEnabled(RoutineExecutor.class)) {
      log_.println(RoutineExecutor.class.getName() + ".run(" + routineName + ") returns null");
    }
    return null;
  }
}
