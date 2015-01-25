package org.adligo.fabricate.models.dependencies;

public interface I_LibraryDependency {

  /*
  public LibraryDependencyMutant(I_ProjectDependency other ) {
    libraryName_ = other.getProjectName();
    platform_ = other.getPlatform();
  } 
   */
  public abstract String getLibraryName();

  public abstract String getPlatform();

}