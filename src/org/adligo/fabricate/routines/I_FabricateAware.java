package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.fabricate.I_Fabricate;

public interface I_FabricateAware {
  public I_Fabricate getFabricate();
  public void setFabricate(I_Fabricate fab);
}
