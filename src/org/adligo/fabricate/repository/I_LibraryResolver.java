package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;

import java.util.List;

public interface I_LibraryResolver {
  /**
   * returns all of the dependencies for the list of libraries
   * @param libs
   * @return
   * @throws IllegalStateException
   */
  public List<I_Dependency> getDependencies(List<LibraryReferenceType> libs) throws IllegalStateException;
  
  /**
   * 
   * @param libName
   * @return the list of dependencies, note
   * it is possible for a dependency to be in the list multiple times.
   * @throws IllegalStateException
   */
  public abstract List<I_Dependency> getDependencies(String libName) throws IllegalStateException;

}