package org.adligo.fabricate.models.common;

import java.util.Set;

/**
 * This interface represents a memory lock
 * for mutation of FabricationMemoryMutant.
 * A memory lock is a allowed list of classes that
 * can mutate the FabricationMemoryMutant instance.
 * @see MemoryLock
 * 
 * @author scott
 *
 */
public interface I_MemoryLock {

  public abstract String getKey();

  public abstract Set<String> getAllowedCallers();

  public abstract boolean isAllowed();

}