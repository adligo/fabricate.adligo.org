package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.i18n.I_GitMessages;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;
import org.adligo.fabricate.common.i18n.I_SystemMessages;

import java.util.Locale;

public class FabricateEnConstants  implements I_FabricateConstants {
  public static final FabricateEnConstants INSTANCE = new FabricateEnConstants();
  
  private FabricateEnConstants() {
  }
  
  @Override
  public I_FileMessages getFileMessages() {
    return FileEnMessages.INSTANCE;
  }
  
  @Override
  public I_GitMessages getGitMessages() {
    return GitEnMessages.INSTANCE;
  }
  
  @Override
  public String getLineSeperator() {
    return System.lineSeparator();
  }
  @Override
  public I_ProjectMessages getProjectMessages() {
    return ProjectEnMessages.INSTANCE;
  }

  @Override
  public boolean isLeftToRight() {
    return true;
  }

  @Override
  public I_SystemMessages getSystemMessages() {
    return SystemEnMessages.INSTANCE;
  }

  @Override
  public String getLanguage() {
    return "en";
  }
  
  @Override
  public String getCountry() {
    return "US";
  }
}
