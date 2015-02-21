package org.adligo.fabricate.models.common;



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
public interface I_FabricationRoutine extends I_Fabrication, Runnable {
  public I_RoutineFactory getTaskFactory();
  
  public void setTaskFactory(I_RoutineFactory factory);
  
  
  /**
   * This method should be used to setup up this routine
   * finding (and casting) all traits, before the routine
   * may is run.  <br/>
   * This method may NOT dialog the user for passwords or other settings.
   * <br/>
   * This method is always the last method called before
   * run is called on all instances which didn't have
   * it's sibling setup(I_FabricationMemoryMutant memory) method
   * called.
   */
  public void setup(I_FabricationMemory memory) throws FabricationRoutineCreationException;
}
