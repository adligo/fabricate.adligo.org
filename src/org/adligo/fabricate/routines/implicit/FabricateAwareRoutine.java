package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_FabricateAware;

public class FabricateAwareRoutine  extends AbstractRoutine 
implements I_FabricateAware {
  
  protected I_Fabricate fabricate_;
  
  @Override
  public I_Fabricate getFabricate() {
    return fabricate_;
  }

  @Override
  public void setFabricate(I_Fabricate fab) {
    fabricate_ = fab;
  }

}
