package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

public interface I_RoutineExecutionEngine {

  public abstract void runRoutines(I_FabricationMemoryMutant<Object> memoryMut)
      throws FabricationRoutineCreationException;

  public abstract boolean hadFailure();

  public abstract Throwable getFailure();

  public abstract I_FabricationRoutine getRoutineThatFailed();

}