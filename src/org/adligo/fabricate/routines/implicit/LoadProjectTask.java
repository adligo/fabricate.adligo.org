package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.I_GitCalls;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_ProjectBriefAware;

/**
 * TODO this class will load the project data (project.xml directory etc) into memory
 * for later usage.
 * 
 * @author scott
 *
 */
public class LoadProjectTask extends AbstractRoutine 
implements I_ProjectBriefAware, I_FabricateAware {
  
  protected I_ProjectBrief brief_;
  protected I_Fabricate fabricate_;
  protected I_GitCalls gitCalls_;
  
  @Override
  public I_ProjectBrief getProjectBrief() {
    return brief_;
  }
  
  @Override
  public void setProjectBrief(I_ProjectBrief brief) {
    brief_ = brief;
  }
  
  @Override
  public I_Fabricate getFabricate() {
    return fabricate_;
  }

  @Override
  public void setFabricate(I_Fabricate fab) {
    fabricate_ = fab;
  }

  @Override
  public boolean setup(I_FabricationMemoryMutant memory, I_RoutineMemoryMutant routineMemory)
      throws FabricationRoutineCreationException {
  //TODO
    return super.setup(memory, routineMemory);
  }

  @Override
  public void setup(I_FabricationMemory memory, I_RoutineMemory routineMemory)
      throws FabricationRoutineCreationException {
    //TODO
    super.setup(memory, routineMemory);
  }


}
