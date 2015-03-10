package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.i18n.I_SystemMessages;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FabricationMemoryMutant <T> implements I_RoutineMemoryMutant<T> {
  private final I_SystemMessages sysMessages_;
  private Map<String,T> map_ = new ConcurrentHashMap<String,T>();
  private Map<String, I_MemoryLock> locks_ = new ConcurrentHashMap<String, I_MemoryLock>();
  
  public FabricationMemoryMutant(I_SystemMessages messages) {
    sysMessages_ = messages;
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
        String message = sysMessages_.getTheMemoryKeyXHasBeenLockedByTheFollowingClasses();
        message = message.replace("<X/>", key);
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
    Exception e = new Exception();
    e.fillInStackTrace();
    StackTraceElement [] elements = e.getStackTrace();
    
    //This code should throw a IndexOutOfBoundsException 
    // if the stack trace is to small, which will probably never occur.
    StackTraceElement ste = elements[1];
    
    String className = ste.getClassName();
    int lastDot = className.lastIndexOf(".");
    String packageName = className.substring(0,  lastDot);
    Set<String> callers =  lock.getAllowedCallers();
    
    boolean checked = false;
    for (String caller: callers) {
      if (caller != null) {
        if (caller.indexOf(packageName) == 0) {
          checked = true;
        } else {
          throw new IllegalArgumentException(sysMessages_.getLocksMayOnlyBeSetOnOrUnderTheCallersPackage());
        }
      }
    } 
    if (!checked) {
      throw new IllegalArgumentException(sysMessages_.getLocksMustContainAtLeastOneAllowedCaller());
    }
    locks_.put(lock.getKey(), lock);
  }
  
  public Map<String,I_MemoryLock> getLocks() {
    return new HashMap<String,I_MemoryLock>(locks_);
  }
}
