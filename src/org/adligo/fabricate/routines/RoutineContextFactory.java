package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.RoutineBrief;

import java.util.Set;

public class RoutineContextFactory implements I_RoutineFactory {
  private RoutineBrief topRoutine_;

  @Override
  public I_FabricationRoutine createRoutine(String name,
      Set<I_ExpectedRoutineInterface> implementedInterfaces) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public I_RoutineFactory createTaskFactory(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  
  

}
