package org.adligo.fabricate.managers;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

public interface I_StageSetup {

  /**
   * Create a stage from a implicit 
   * stage, fabricate.xml or a merge using a facets name.
   * @param name
   * @return
   * @throws FabricationRoutineCreationException
   */
  public abstract I_FabricationRoutine processStageSetup(String name)
      throws FabricationRoutineCreationException;

}