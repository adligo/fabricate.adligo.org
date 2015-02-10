package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface I_DependenciesNormalizer {

  public abstract void add(Collection<I_Dependency> depsIn);

  public abstract ConcurrentLinkedQueue<I_Dependency> get();

}