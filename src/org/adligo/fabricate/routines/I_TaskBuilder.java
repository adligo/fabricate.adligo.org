package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.util.Set;

/**
 * This interface helps build most routines
 * by creating them with the task factory
 * @author scott
 *
 */
public interface I_TaskBuilder {
  /**
   * 
   * @param name
   * @param implementedInterfaces the interfaces that 
   * the client code to this method expects the return 
   * value to implement.  
   * @return a fabrication routine
   */
  public I_FabricationRoutine buildTask(String name, 
      Set<I_ExpectedRoutineInterface> implementedInterfaces) 
      throws FabricationRoutineCreationException;
  
  public I_FabricationRoutine buildInitialTask(String name, 
      Set<I_ExpectedRoutineInterface> implementedInterfaces) 
      throws FabricationRoutineCreationException;
}
