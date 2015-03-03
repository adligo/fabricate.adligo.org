package org.adligo.fabricate.models.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricationMemoryMutant <T> implements I_RoutineMemoryMutant<T> {
  public static final String THE_KEY_X_HAS_BEEN_LOCKED_BY_THE_FOLLOWING_CLASSES = "The key '<X/>' has been locked by the following classes;";
  private Map<String,T> map_ = new ConcurrentHashMap<String,T>();
  private Map<String, I_MemoryLock> locks_ = new ConcurrentHashMap<String, I_MemoryLock>();
  
  public FabricationMemoryMutant() {
  }
  
  @Override
  public T get(String key) {
    return map_.get(key);
  }

  @Override
  public void put(String key, T value) {
    I_MemoryLock lock = locks_.get(key);
    if (lock != null) {
      if (!lock.isAllowed()) {
        String message = THE_KEY_X_HAS_BEEN_LOCKED_BY_THE_FOLLOWING_CLASSES.replace("<X/>", key);
        throw new IllegalStateException(message + 
            System.lineSeparator() + lock.getAllowedCallers());
      }
    }
    map_.put(key, value);
  }

  @Override
  public Map<String, T> get() {
    return map_;
  }

  @Override
  public void addLock(I_MemoryLock lock) {
    locks_.put(lock.getKey(), lock);
  }
  
  public Map<String,I_MemoryLock> getLocks() {
    return new HashMap<String,I_MemoryLock>(locks_);
  }
}
