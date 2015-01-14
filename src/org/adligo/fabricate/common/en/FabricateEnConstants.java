package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;

public class FabricateEnConstants  implements I_FabricateConstants {
  public static final FabricateEnConstants INSTANCE = new FabricateEnConstants();
  
  private FabricateEnConstants() {
  }
  
  @Override
  public I_ProjectMessages getProjectMessages() {
    return ProjectEnMessages.INSTANCE;
  }

  @Override
  public I_FileMessages getFileMessages() {
    return FileEnMessages.INSTANCE;
  }

  @Override
  public String getLineSeperator() {
    return System.lineSeparator();
  }
  
  
}
