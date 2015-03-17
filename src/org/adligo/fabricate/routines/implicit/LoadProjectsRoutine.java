package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.Project;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LoadProjectsRoutine extends ProjectBriefQueueRoutine
{
  

  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    return super.setup(memory, routineMemory);
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    super.setup(memory, routineMemory);
  }

}
