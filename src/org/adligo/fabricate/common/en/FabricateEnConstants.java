package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_AttributeConstants;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.i18n.I_GitMessages;
import org.adligo.fabricate.common.i18n.I_ImplicitTraitMessages;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;
import org.adligo.fabricate.common.i18n.I_SystemMessages;

public class FabricateEnConstants  implements I_FabricateConstants {
  private static final String EN = "en";
  private static final String US = "US";
  private static final String EXTRACT = "extract";
  public static final FabricateEnConstants INSTANCE = new FabricateEnConstants();
  
  private FabricateEnConstants() {
  }
  
  @Override
  public I_AttributeConstants getAttributeConstants() {
    return AttributeEnConstants.INSTANCE;
  }
  
  @Override
  public I_CommandLineConstants getCommandLineConstants() {
    return CommandLineEnConstants.INSTANCE;
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
  public  I_ImplicitTraitMessages getImplicitTraitMessages() {
    return ImplicitTraitEnMessages.INSTANCE;
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
    return EN;
  }
  
  @Override
  public String getCountry() {
    return US;
  }

  @Override
  public String getExtractDirName() {
    return EXTRACT;
  }

}
