package org.adligo.fabricate.common;

public class DepotEntryMutant implements I_DepotEntry {

  /**
   * This is the type of the artifact
   * i.e. jar
   * 
   */
  private String artifactType_;
  /**
   * This is the group of the 
   * project i.e. fabricat.adligo.org
   */
  private String projectName_;
  
  public DepotEntryMutant() {}

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.I_DepotEntry#getArtifactType()
   */
  @Override
  public String getArtifactType() {
    return artifactType_;
  }
  public void setArtifactType(String artifactType) {
    this.artifactType_ = artifactType;
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
  

}
