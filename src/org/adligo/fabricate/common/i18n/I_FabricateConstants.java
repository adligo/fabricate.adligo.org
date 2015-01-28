package org.adligo.fabricate.common.i18n;

public interface I_FabricateConstants {
  public String getLanguage();
  public String getCountry();
  public boolean isLeftToRight();
  public I_CommandLineConstants getCommandLineConstants();
  public I_FileMessages getFileMessages();
  public I_GitMessages getGitMessages();
  /**
   * Try to use I_FabSystem.lineSeperator
   * instead of this method if possible.
   * @return
   */
  public String getLineSeperator();
  public I_ProjectMessages getProjectMessages();
  public I_SystemMessages getSystemMessages();
  
} 
