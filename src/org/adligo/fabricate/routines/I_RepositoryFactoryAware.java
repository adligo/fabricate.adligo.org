package org.adligo.fabricate.routines;

import org.adligo.fabricate.repository.I_RepositoryFactory;

public interface I_RepositoryFactoryAware {
  public I_RepositoryFactory getRepositoryFactory();
  public void setRepositoryFactory(I_RepositoryFactory repositoryFactory);
}
