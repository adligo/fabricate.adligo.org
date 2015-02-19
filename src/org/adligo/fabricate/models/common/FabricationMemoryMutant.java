package org.adligo.fabricate.models.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricationMemoryMutant implements I_FabricationMemoryMutant {
  private Map<String,Object> map = new ConcurrentHashMap<String,Object>();

  @Override
  public Object get(String key) {
    return map.get(key);
  }

  @Override
  public void put(String key, Object value) {
    map.put(key, value);
  }

  @Override
  public Map<String, Object> get() {
    return map;
  }
}
