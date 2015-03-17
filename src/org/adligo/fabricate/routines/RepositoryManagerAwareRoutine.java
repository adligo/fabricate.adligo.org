package org.adligo.fabricate.routines;

import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.implicit.FabricateAwareRoutine;

public class RepositoryManagerAwareRoutine extends FabricateAwareRoutine implements I_RepositoryManagerAware {
  protected I_RepositoryManager repositoryManager_;
  
  @Override
  public I_RepositoryManager getRepositoryManager() {
    return repositoryManager_;
  }

  @Override
  public void setRepositoryManager(I_RepositoryManager repoMananager) {
    repositoryManager_ = repoMananager;
  }

}
