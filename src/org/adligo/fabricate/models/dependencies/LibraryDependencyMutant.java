package org.adligo.fabricate.models.dependencies;

public class LibraryDependencyMutant implements I_LibraryDependency {
  private String libraryName_;
  private String platform_;
  
  public LibraryDependencyMutant() {}
  
  public LibraryDependencyMutant(I_LibraryDependency other ) {
    libraryName_ = other.getLibraryName();
    platform_ = other.getPlatform();
  } 
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_LibraryDependency#getLibraryName()
   */
  @Override
  public String getLibraryName() {
    return libraryName_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_LibraryDependency#getPlatform()
   */
  @Override
  public String getPlatform() {
    return platform_;
  }
  
  public void setLibraryName(String name) {
    this.libraryName_ = name;
  }
  
  public void setPlatform(String platform) {
    this.platform_ = platform;
  }
  
}
