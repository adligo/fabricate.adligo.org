package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.fabricate.I_FabricateXmlDiscovery;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;

public interface I_Fabrication {
  public I_RoutineBrief getBrief();
  public String getCaller();
  public I_RoutineFactory getTraitFactory();
  /**
   * This should return something like;<br/>
   * caller + getBrief().getName();<br/>
   * or<br/>
   * caller + "/" + getBrief().getName();<br/>
   * when the caller has a path ('/') already.
   * @return
   */
  public String getStack();
  public I_FabSystem getSystem();
  public I_FabricateXmlDiscovery getLocations();
  
  
  public void setBrief(I_RoutineBrief brief);
  /**
   * The caller is the Fabricate routine surrogate for a stack
   * for stack traces, so that wait messages can
   * specify what is happening to the user.
   * @param caller
   */
  public void setCaller(String caller);
  public void setLocations(I_FabricateXmlDiscovery locations);
  public void setTraitFactory(I_RoutineFactory factory);
  public void setSystem(I_FabSystem system);
  
  /**
   * This method should be used to setup up this routine
   * finding (and casting) all traits specific to the instance.
   * This method may write values to memory for subsequent instances
   * of the class with this implementation.
   * This method may dialog the user for passwords or other settings.
   * 
   * This method is always the last method called on the 
   * first instance of the class with this implementation
   * before it is run.
   * @return a boolean if this fabrication should run, this allows the 
   * stages to check for command line parameters or other indicators
   * to see if they should run.
   */
  public boolean setup(I_FabricationMemoryMutant memory) throws FabricationRoutineCreationException;
}
