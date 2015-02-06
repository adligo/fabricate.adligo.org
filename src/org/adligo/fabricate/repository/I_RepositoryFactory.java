package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface I_RepositoryFactory {
  public I_RepositoryPathBuilder create(String remoteRepository);
  public I_RepositoryPathBuilder create(String remoteRepository, String separator);
  public I_DependenciesManager createDependenciesManager(I_FabSystem sys, 
      ConcurrentLinkedQueue<I_Dependency> deps);
  public I_DependencyManager createDependencyManager(
      I_FabSystem sys, List<String> repos, I_RepositoryPathBuilder builder);
}
