package org.adligo.fabricate.common.i18n;

public interface I_FabricateConstants {
  public String getLanguage();
  public String getCountry();
  public boolean isLeftToRight();
  /**
   * This is the relative folder or directory name
   * of the folder where Fabricate extracted
   * jar or other zip files that it was requested to
   * extract (see xml dependency type).  
   * For English this is 'extract', and it should 
   * generally match between the xml schema for the language
   * and the .property files for the language.
   * 
   * If your attempting to translate Fabricate into another 
   * language, it is suggested that you start with the xml
   * schemas first, since they are all in English.  There
   * are some comments in fabricate_v1_0.xsd.
   * @return
   */
  public String getExtractDirName();
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
