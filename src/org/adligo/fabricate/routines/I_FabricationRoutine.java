package org.adligo.fabricate.routines;

/**
 * This is the base interface of all things that
 * can be done with Fabricate.  Commands and Stages 
 * are always I_FabricationRoutines.  I_FabricationRoutine
 * implementations must always have a zero argument
 * constructor so they can be created through reflection.
 * 
 * @author scott
 *
 */
public interface I_FabricationRoutine extends Runnable {
  /**
   * If this routine is concurrent, which is usually
   * true since Fabricate favors concurrency for 
   * most building.   This may be set to false
   * for simple routines which only deal with one thing.
   * A good example of this is the SimpleCryptCommand which 
   * simply encrypts a password and prints the result 
   * to the console.
   * @return
   */
  public boolean isConcurrent();
}
