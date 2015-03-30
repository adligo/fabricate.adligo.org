package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.repository.I_RepositoryManager;

public interface I_RepositoryManagerAware extends I_FabricationRoutine {
  public I_RepositoryManager getRepositoryManager();
  public void setRepositoryManager(I_RepositoryManager repoMananager);
}
