package org.adligo.fabricate.models.common;

import java.util.Set;

/**
 * This allows runtime linking of classes by name
 * to I_FabricationRoutine instances.  These are
 * used to share code between commands and stage
 * tasks.  Traits are either set from the 
 * trait xml node in fabricate.xml, or defined from 
 * a command, stage or task.  When a command, stage
 * or task has a class setting then the class is used
 * otherwise fabricate looks to find a trait from the 
 * name using fabricate.xml or it's implicit traits and returns that.
 *   Fabricate comes with some implicit traits,
 * commands, stages and tasks.  These are all located
 * in the org.adligo.fabricate.routines.implicit package.
 * When traits are added to fabricate.xml using the same name
 * as a implicit trait, they override it.
 * 
 * @author scott
 */
public interface I_RoutineFactory {
  /**
   * 
   * @param name
   * @param implementedInterfaces the interfaces that 
   * the client code to this method expects the return 
   * value to implement.  
   * @return a fabrication routine
   */
  public I_FabricationRoutine createRoutine(String name, 
      Set<I_ExpectedRoutineInterface> implementedInterfaces) 
      throws FabricationRoutineCreationException;
  
  public I_RoutineFactory createTaskFactory(String name);
}
