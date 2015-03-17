package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class filters the dependencies, so that a overlap of dependencies where one is set to expand and another that equals and hashCodes
 * does NOT. This enables the dependency down loader to have a unique queue of dependencies that don't overlap.
 * This also enables future logic which may go for the lowest common denominator approach when deciding which
 * dependencies need to be down-loaded.  
 * 
 * @author scott
 *
 */
public class DependenciesFilter {
  private Map<I_Dependency, I_Dependency> dependencies_;
  
  public DependenciesFilter() {
    dependencies_ = new ConcurrentHashMap<I_Dependency, I_Dependency>();
  }
  
  public synchronized void add(Collection<I_Dependency> deps) {
    for (I_Dependency d: deps) {
      I_Dependency currentDep = dependencies_.get(d);
      if (currentDep == null) {
        dependencies_.put(d, d);
      } else {
        if (!currentDep.isExtract() && d.isExtract()) {
          dependencies_.put(d, d);
        }
      }
    }
  }
  
  public synchronized List<I_Dependency> get() {
    List<I_Dependency> toRet = new ArrayList<I_Dependency>();
    toRet.addAll(dependencies_.values());
    return Collections.unmodifiableList(toRet);
  }
}
