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
public interface I_FabricationRoutine extends Runnable {
  
}
