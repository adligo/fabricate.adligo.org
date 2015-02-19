package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_ImplicitTraitMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.fabricate.I_FabricateXmlDiscovery;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;

public class AbstractRoutine implements I_FabricationRoutine {
  protected I_RoutineBrief brief_;
  protected String caller_;
  protected I_FabSystem system_;
  protected I_FabricateConstants constants_;
  
  protected I_ImplicitTraitMessages implicit_;
  protected I_FabLog log_;
  protected I_RoutineFactory traitFactory_;
  protected I_RoutineFactory taskFactory_;
  protected I_FabricateXmlDiscovery locations_;
  
  /**
   * Do nothing, allow extension classes to override.
   */
  @Override
  public void run() {
  }
  
  public I_RoutineBrief getBrief() {
    return brief_;
  }
  
  public String getCaller() {
    return caller_;
  }
  
  @Override
  public I_RoutineFactory getTaskFactory() {
    return taskFactory_;
  }
  @Override
  public I_RoutineFactory getTraitFactory() {
    return traitFactory_;
  }

  @Override
  public String getStack() {
    //TODO
    return null;
  }
  
  @Override
  public I_FabSystem getSystem() {
    return system_;
  }
  @Override
  public void setTaskFactory(I_RoutineFactory factory) {
    taskFactory_ = factory;
  }
  
  @Override
  public void setTraitFactory(I_RoutineFactory factory) {
    traitFactory_ = factory;
  }

  @Override
  public void setSystem(I_FabSystem system) {
    system_ = system;
    log_ = system.getLog();
    constants_ = system.getConstants();
    implicit_ = constants_.getImplicitTraitMessages();
  }

  public void setBrief(I_RoutineBrief brief) {
    this.brief_ = brief;
  }

  public void setCaller(String caller) {
    this.caller_ = caller;
  }

  @Override
  public I_FabricateXmlDiscovery getLocations() {
    return locations_;
  }

  @Override
  public void setLocations(I_FabricateXmlDiscovery locations) {
    locations_ = locations;
  }

  /**
   * Do nothing, allow extension classes to override.
   */
  @Override
  public boolean setup(I_FabricationMemoryMutant memory) throws FabricationRoutineCreationException {
    return true;
  }

  /**
   * Do nothing, allow extension classes to override.
   */
  @Override
  public void setup(I_FabricationMemory memory) throws FabricationRoutineCreationException {
    
  }
  
}
