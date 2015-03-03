package org.adligo.fabricate.models.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FabricationMemory <T> implements I_RoutineMemory <T> {
  private final Map<String,T> immutable_;

  @SuppressWarnings("unchecked")
  public FabricationMemory(I_FabricationMemory<T> other) {
    Map<String,T> map = new HashMap<String,T>(other.get());
    Object env =  map.get(FabricationMemoryConstants.ENV);
    if (env instanceof ExecutionEnvironmentMutant) {
      map.put(FabricationMemoryConstants.ENV, (T) new ExecutionEnvironment(
          (ExecutionEnvironmentMutant) env));
    }
    immutable_ = Collections.unmodifiableMap(map);
  }
  
  @Override
  public T get(String key) {
    return immutable_.get(key);
  }

  @Override
  public Map<String, T> get() {
    return immutable_;
  }

}
