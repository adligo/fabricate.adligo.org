package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

public interface I_RoutineExecutorFactory {
  public I_FabricationRoutine create(I_FabricationMemoryMutant memory) throws FabricationRoutineCreationException;
  public I_FabricationRoutine create(I_FabricationMemory memory) throws FabricationRoutineCreationException;
}
