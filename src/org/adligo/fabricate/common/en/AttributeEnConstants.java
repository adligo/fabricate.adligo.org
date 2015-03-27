package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_AttributeConstants;

public class AttributeEnConstants implements I_AttributeConstants {
  
  

  private static final String PLATFORMS = "platforms";

  public static final AttributeEnConstants INSTANCE = new AttributeEnConstants();
  
  private static final String EXCLUDE = "exclude";
  private static final String FILES = "files";
  private static final String GIT_DEFAULT_BRANCH = "Git: default branch";
  private static final String INCLUDE = "include";
  private static final String IDE = "ide";
  
  private static final String SRC_DIRS = "srcDirs";
  private static final String JDK_SRC_DIR = "jdkSrcDir";
  
  private AttributeEnConstants() {}
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getExclude()
   */
  @Override
  public String getExclude() {
    return EXCLUDE;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getFiles()
   */
  @Override
  public String getFiles() {
    return FILES;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getGitDefaultBranch()
   */
  @Override
  public String getGitDefaultBranch() {
    return GIT_DEFAULT_BRANCH;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getIde()
   */
  @Override
  public String getIde() {
    return IDE;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getInclude()
   */
  @Override
  public String getInclude() {
    return INCLUDE;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getPlatforms()
   */
  @Override
  public String getPlatforms() {
    return PLATFORMS;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getSrcDirs()
   */
  @Override
  public String getSrcDirs() {
    return SRC_DIRS;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getJdkSrcDir()
   */
  @Override
  public String getJdkSrcDir() {
    return JDK_SRC_DIR;
  }
}
