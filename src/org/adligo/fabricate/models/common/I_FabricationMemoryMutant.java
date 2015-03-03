package org.adligo.fabricate.models.common;

public interface I_FabricationMemoryMutant <T> extends I_FabricationMemory<T> {
  /**
   * add a memory lock which keeps plug-ins
   * from corrupting memory required by Fabricate.
   * @param lock
   */
  public void addLock(I_MemoryLock lock);
  /**
   * note this may throw a IllegalStateException
   * when a key is not allowed to be set due to a lock.
   * @param key
   * @param value
   */
  public void put(String key, T value);
}
