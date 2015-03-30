package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.system.I_FailureTransport;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;

public interface I_RoutineExecutor {

  public abstract I_FailureTransport run(String routineName, I_RoutineBuilder builder,
      FabricationMemoryMutant<Object> memory);

}