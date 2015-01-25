package org.adligo.fabricate.models.dependencies;

public class LibraryDependency implements I_LibraryDependency {
  private String libraryName_;
  private String platform_;
  
  public LibraryDependency() {}
  
  public LibraryDependency(I_LibraryDependency other ) {
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

}
