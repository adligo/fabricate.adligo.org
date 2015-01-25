package org.adligo.fabricate.models.dependencies;

public class ProjectDependency implements I_ProjectDependency {
  private String projectName_;
  private String platform_;
  
  public ProjectDependency() {}
  
  public ProjectDependency(I_ProjectDependency other ) {
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
}
