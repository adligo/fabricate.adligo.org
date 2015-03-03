package org.adligo.fabricate.models.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @see I_MemoryLock
 * @author scott
 *
 */
public class MemoryLock implements I_MemoryLock {
  public String key_;
  public Set<String> allowedCallers_;
  
  public MemoryLock(String key, Collection<String> allowedCallers) {
    key_ = key;
    if (allowedCallers != null) {
      allowedCallers_ = Collections.unmodifiableSet(
          new HashSet<String>(allowedCallers));
    }
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_FabricationMemoryLock#getKey()
   */
  @Override
  public String getKey() {
    return key_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_FabricationMemoryLock#getAllowedCallers()
   */
  @Override
  public Set<String> getAllowedCallers() {
    return allowedCallers_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_FabricationMemoryLock#isAllowed()
   */
  @Override
  public boolean isAllowed() {
    Exception x = new Exception();
    x.fillInStackTrace();
    StackTraceElement [] stack =  x.getStackTrace();
    if (stack.length < 2) {
      return false;
    }
    //Note this stack contains
    //0 this class
    //1 FabricationMemoryMutant 
    //2 the class that called FabricationMemoryMutant
    StackTraceElement e = stack[2];
    String clazz = e.getClassName();
    if (allowedCallers_.contains(clazz)) {
      return true;
    }
    return false;
  }
}
