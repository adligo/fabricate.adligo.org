package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

public interface I_DependencyManager {

  public abstract boolean manange(I_Dependency dep);

  public abstract void setLocalRepository(String localRepository);

  public abstract void setConfirmIntegrity(boolean confirmIntegrity);

  public abstract void setFactory(I_RepositoryFactory factory);

}