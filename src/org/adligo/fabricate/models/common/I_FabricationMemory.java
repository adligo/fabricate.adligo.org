package org.adligo.fabricate.models.common;

import java.util.Map;

/**
 * This interface is used to pass data between
 * I_Fabrication instances of the same type.
 * 
 * @author scott
 *
 */
public interface I_FabricationMemory <T> {
  public Map<String,T> get();
  /**
   * @param key
   * @return
   */
  public T get(String key);
}
