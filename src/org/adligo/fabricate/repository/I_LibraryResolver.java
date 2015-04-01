package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.DependencyVersionMismatchException;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;

import java.util.List;

public interface I_LibraryResolver {
  /**
   * returns all of the dependencies for the list of libraries
   * @param libs
   * @param the project name, or null for resolving libraries from fabricate.xml.
   * @return
   * @throws IllegalStateException
   */
  public List<I_Dependency> getDependencies(List<LibraryReferenceType> libs, String projectName) 
      throws IllegalStateException, DependencyVersionMismatchException;
  
  /**
   * 
   * @param libName
   * @param projectName the project name, or null for resolving libraries from fabricate.xml.
   * @param platform the platform name from the LibraryReferenctType or null,
   *   if present the platform will overwrite the value on each dependency.
   *   This gives the projects the ability to only depend on libraries for specific
   *   platforms with out the library dependencies being aware of the platform.
   *   
   * @return the list of dependencies, note
   * it is possible for a dependency to be in the list multiple times.
   * @throws IllegalStateException
   */
  public abstract List<I_Dependency> getDependencies(String libName, String projectName, String platform) 
      throws IllegalStateException, DependencyVersionMismatchException ;

}