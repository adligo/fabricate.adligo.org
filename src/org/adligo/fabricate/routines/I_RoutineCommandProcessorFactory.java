package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.util.Set;

public interface I_RoutineCommandProcessorFactory {

  public abstract I_FabricationRoutine createCommand(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;

  public abstract RoutineFactory getCommands();
}