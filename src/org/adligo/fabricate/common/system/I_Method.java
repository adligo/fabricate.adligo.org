package org.adligo.fabricate.common.system;

import java.lang.reflect.InvocationTargetException;

/**
 * stub for java.lang.reflect.Method
 * @author scott
 *
 */
public interface I_Method {
  public Object invoke(Object obj, Object... args) throws 
    IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
