package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class helps identify a unique
 * set of dependencies.  Dependencies are 
 * considered unique using the DependencyMutant
 * equals and hash code methods.  Identical dependencies
 * are removed, and when two identical dependencies
 * have different extract settings the dependency 
 * with extract set to true is used.
 * This class is NOT thread safe.
 * 
 * @author scott
 *
 */
public class DependenciesNormalizer implements I_DependenciesNormalizer {
  private Set<I_Dependency> deps_ = new HashSet<I_Dependency>();
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependenciesNormalizer#add(java.util.Collection)
   */
  @Override
  public void add(Collection<I_Dependency> depsIn) {
    for (I_Dependency dep: depsIn) {
      if (!deps_.contains(dep)) {
        deps_.add(dep);
      } else {
        if (dep.isExtract()) {
          //replace the dependency with the one with extract set to true
          deps_.remove(dep);
          deps_.add(dep);
        }
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependenciesNormalizer#get()
   */
  @Override
  public ConcurrentLinkedQueue<I_Dependency> get() {
    return new ConcurrentLinkedQueue<I_Dependency>(deps_);
  }
}
