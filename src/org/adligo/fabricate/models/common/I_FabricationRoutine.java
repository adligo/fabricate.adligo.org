package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;

/**
 * This is the base interface of all things that
 * can be done with Fabricate.  Commands, Stages and Tasks 
 * are always I_FabricationRoutines.  I_FabricationRoutine
 * implementations must always have a zero argument
 * constructor so they can be created through reflection.
 *   Note there are several other interfaces that 
 * a I_FabricationRoutine may implement for various 
 * purposes.  These are in org.adligo.fabricate.routines
 * so that they may depend on other downstream dependency classes like 
 * I_Fabricate, I_Project.
 * @author scott
 *
 */
public interface I_FabricationRoutine extends Runnable {
  public I_RoutineBrief getBrief();
  public I_RoutineFactory getTraitFactory();
  public I_RoutineFactory getTaskFactory();
  public I_FabSystem getSystem();
  
  public void setBrief(I_RoutineBrief brief);
  public void setTraitFactory(I_RoutineFactory factory);
  public void setTaskFactory(I_RoutineFactory factory);
  public void setSystem(I_FabSystem system);
  
  /**
   * This method should be used to setup up this routine
   * finding (and casting) all traits, before the routine
   * may be run on another thread.  It is always the 
   * last method to call on the main thread before
   * run is called on the main thread or on another thread.
   */
  public void setup() throws FabricationRoutineCreationException;
}
