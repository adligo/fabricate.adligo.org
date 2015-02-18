package org.adligo.fabricate.routines;

import java.util.concurrent.ConcurrentHashMap;

public interface I_MemoryAware {
  public void setMemory(ConcurrentHashMap<String, Object> memory);
}
