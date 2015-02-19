package org.adligo.fabricate.models.common;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricationMemory implements I_FabricationMemory {
  private final Map<String,Object> immutable_;
  private Map<String,Object> map_ = new ConcurrentHashMap<String,Object>();

  public FabricationMemory(I_FabricationMemory other) {
    immutable_ = Collections.unmodifiableMap(other.get());
    map_.putAll(immutable_);
  }
  
  @Override
  public Object get(String key) {
    return map_.get(key);
  }

  @Override
  public Map<String, Object> get() {
    return immutable_;
  }

}
