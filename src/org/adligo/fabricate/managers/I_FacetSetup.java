package org.adligo.fabricate.managers;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

public interface I_FacetSetup {

  /**
   * Create a facet from the implicit obtain
   * facet, fabricate.xml or a merge using a facets name.
   * @param name
   * @return
   * @throws FabricationRoutineCreationException
   */
  public abstract I_FabricationRoutine processFacetSetup(String name)
      throws FabricationRoutineCreationException;

}