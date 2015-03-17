package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface I_RepositoryManager {

  public abstract void manageDependencies();

  public abstract void checkRepositories();

  public abstract ConcurrentLinkedQueue<I_Dependency> getDependencies();

  public abstract void addDependencies(ConcurrentLinkedQueue<I_Dependency> dependencies);

}