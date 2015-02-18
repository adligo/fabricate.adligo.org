package org.adligo.fabricate.routines;

import java.util.List;

/**
 * This interface is for ensuring that 
 * the generic type is compatible with
 * the expected generic type at runtime
 * for classes like I_InputAware.
 * @author scott
 *
 * @param <T>
 */
public interface I_GenericTypeAware {
  /**
   * 
   * @param interfaceClass
   * @return the list of classes that are generic types of the
   * parameter class, so the EncryptTrait should return a single String class list
   * when the I_InputAware class is passed in, since EncryptTrait 
   * implements I_InputAware<String>.
   */
  public List<Class<?>> getClassType(Class<?> interfaceClass);
}
