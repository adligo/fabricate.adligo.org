package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_AttributeConstants;

public class AttributeEnConstants implements I_AttributeConstants {
  
  public static final AttributeEnConstants INSTANCE = new AttributeEnConstants();
  
  private static final String EXCLUDE = "exclude";
  private static final String FILES = "files";
  private static final String GIT_DEFAULT_BRANCH = "Git: default branch";
  private static final String INCLUDE = "include";

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
   * @see org.adligo.fabricate.common.en.I_AttributeConstants#getInclude()
   */
  @Override
  public String getInclude() {
    return INCLUDE;
  }
  
}
