package org.adligo.fabricate.models.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This helps Fabricate fail fast when
 * a routine is found by name with different
 * interfaces (or the same interfaces with different
 * generic types).
 * 
 * @author scott
 *
 */
public class ExpectedRoutineInterface implements I_ExpectedRoutineInterface {
  private final Class<?> interfaceClass_;
  private final List<Class<?>> genericTypes_;
  
  public ExpectedRoutineInterface(Class<?> interfaceClass, Class<?> ... types) {
    if (interfaceClass == null) {
      throw new NullPointerException();
    }
    interfaceClass_ = interfaceClass;
    if (types != null) {
      if (types.length >= 1) {
        List<Class<?>> newGts = new ArrayList<Class<?>>();
        for (int i = 0; i < types.length; i++) {
          Class<?> type = types[i];
          if (type != null) {
            newGts.add(type);
          }
        }
        genericTypes_ = Collections.unmodifiableList(newGts);
        return;
      }
    }
    genericTypes_ = Collections.emptyList();
  }
  
  public ExpectedRoutineInterface(I_ExpectedRoutineInterface other) {
    interfaceClass_ = other.getInterfaceClass();
    if (interfaceClass_ == null) {
      throw new NullPointerException();
    }
    List<Class<?>> gts = other.getGenericTypes();
    
    if (gts != null) {
      if (gts.size() > 0) {
        List<Class<?>> newGts = new ArrayList<Class<?>>();
      
        for (Class<?> inter: gts) {
          if (inter != null) {
            newGts.add(inter);
          }
        }
        genericTypes_ = Collections.unmodifiableList(gts);
        return;
      }
    }
    genericTypes_ = Collections.emptyList();
  }
  
  @Override
  public boolean equals(Object o) {
    return ExpectedRoutineInterfaceMutant.equals(this, o);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_ExpectedRoutineInterface#getInterfaceClass()
   */
  @Override
  public Class<?> getInterfaceClass() {
    return interfaceClass_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_ExpectedRoutineInterface#getGenericTypes()
   */
  @Override
  public List<Class<?>> getGenericTypes() {
    return genericTypes_;
  }
  
  @Override
  public int hashCode() {
    return ExpectedRoutineInterfaceMutant.hashCode(this);
  }
}
