package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.io.IOException;

public interface I_DependencyManager {

  /**
   * 
   * @param dep
   * @throws DependencyNotAvailableException if all remote repositories seem to 
   * either non contain this dependency or if the dependency is malformed on all 
   * remote repositories.
   * @throws IOException if there is a local IO issue, reading, deleting or extracting files.
   */
  public abstract void manange(I_Dependency dep) throws DependencyNotAvailableException, IOException;

  public abstract void setLocalRepository(String localRepository);

  public abstract void setConfirmIntegrity(boolean confirmIntegrity);

  public abstract void setFactory(I_RepositoryFactory factory);

}