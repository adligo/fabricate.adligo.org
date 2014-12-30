package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;

public class FabricateEnConstants  implements I_FabricateConstants {
  private ProjectEnMessages proj = new ProjectEnMessages();

  @Override
  public I_ProjectMessages getProjectMessages() {
    return proj;
  }
  
  
}
