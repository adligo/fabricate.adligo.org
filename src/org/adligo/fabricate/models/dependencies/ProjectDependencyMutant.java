package org.adligo.fabricate.models.dependencies;

public class ProjectDependencyMutant implements I_ProjectDependency {
  private String projectName_;
  private String platform_;
  
  public ProjectDependencyMutant() {}
  
  public ProjectDependencyMutant(I_ProjectDependency other ) {
    projectName_ = other.getProjectName();
    platform_ = other.getPlatform();
  } 
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_ProjectDependency#getProjectName()
   */
  @Override
  public String getProjectName() {
    return projectName_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_ProjectDependency#getPlatform()
   */
  @Override
  public String getPlatform() {
    return platform_;
  }
  public void setProjectName(String projectName) {
    this.projectName_ = projectName;
  }
  public void setPlatform(String platform) {
    this.platform_ = platform;
  }
  
}
