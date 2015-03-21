package org.adligo.fabricate.depot;


public class ArtifactMutant implements I_Artifact {

  private String fileName_;
  
  private String platformName_;
  
  
  /**
   * This is the group of the 
   * project i.e. fabricat.adligo.org
   */
  private String projectName_;
  
  /**
   * This is the type of the artifact
   * i.e. jar
   * 
   */
  private String type_;

  
  public ArtifactMutant() {}

  /* (non-Javadoc)
   * @see org.adligo.fabricate.depot.I_Artifact#getType()
   */
  @Override
  public String getType() {
    return type_;
  }
  
  public void setType(String type) {
    this.type_ = type;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.I_DepotEntry#getProjectName()
   */
  @Override
  public String getProjectName() {
    return projectName_;
  }
  
  public void setProjectName(String projectName) {
   projectName_ = projectName;
  }

  public String getFileName() {
    return fileName_;
  }

  public String getPlatformName() {
    return platformName_;
  }

  public void setFileName(String fileName) {
    this.fileName_ = fileName;
  }

  public void setPlatformName(String platform) {
    this.platformName_ = platform;
  }
  

}
