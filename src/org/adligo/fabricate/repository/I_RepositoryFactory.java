package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface I_RepositoryFactory {

  public I_DependenciesManager createDependenciesManager(I_FabSystem sys, 
      ConcurrentLinkedQueue<I_Dependency> deps);
  public I_DependencyManager createDependencyManager(
      I_FabSystem sys, List<String> repos, I_RepositoryPathBuilder builder);
  public I_LibraryResolver createLibraryResolver(I_FabSystem sys, I_Fabricate fabricate);
  public I_RepositoryPathBuilder createRepositoryPathBuilder(String remoteRepository);
  public I_RepositoryPathBuilder createRepositoryPathBuilder(String remoteRepository, String separator);
}
