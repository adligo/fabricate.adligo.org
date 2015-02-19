package org.adligo.fabricate.models.common;

import java.util.Map;

/**
 * This interface is used to pass data between
 * I_Fabrication instances of the same type.
 * 
 * @author scott
 *
 */
public interface I_FabricationMemory {
  public Map<String,Object> get();
  /**
   * @param key
   * @return
   */
  public Object get(String key);
}
