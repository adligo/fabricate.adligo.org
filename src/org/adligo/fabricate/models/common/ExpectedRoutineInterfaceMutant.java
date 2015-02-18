package org.adligo.fabricate.models.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
public class ExpectedRoutineInterfaceMutant implements I_ExpectedRoutineInterface {
  public static int hashCode(I_ExpectedRoutineInterface i) {
    final int prime = 31;
    int result = 1;
    Class<?> clazz = i.getInterfaceClass();
    result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
    List<Class<?>> types = i.getGenericTypes();
    if (types != null && types.size() >= 1) {
      for (Class<?> type: types) {
        result = prime * result + ((type == null) ? 0 : type.hashCode());
      }
    }
    return result;
  }

  public static boolean equals(I_ExpectedRoutineInterface me, Object obj) {
    if (me == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    I_ExpectedRoutineInterface other = (I_ExpectedRoutineInterface) obj;
    Class<?> interfaceClass = me.getInterfaceClass();
    Class<?> otherInterfaceClass = other.getInterfaceClass();
    if (interfaceClass == null) {
      if (otherInterfaceClass != null) {
        return false;
      }
    } else if (interfaceClass.equals(otherInterfaceClass)) {
      return false;
    }
    
    List<Class<?>> types = me.getGenericTypes();
    List<Class<?>> otherTypes = other.getGenericTypes();
    if (types == null) {
      if (otherTypes != null) {
        return false;
      }
    }
    if (otherTypes == null) {
      return false;
    }
    if (types.size() != otherTypes.size()) {
      return false;
    }
    Iterator<Class<?>> ti = types.iterator();
    Iterator<Class<?>> oti = otherTypes.iterator();
    while (ti.hasNext()) {
      Class<?> type = ti.next();
      Class<?> otherType = oti.next();
      if (!type.getName().equals(otherType.getName())) {
        return false;
      }
    }
    return true;
  }
  
  private Class<?> interfaceClass_;
  private List<Class<?>> genericTypes_;
  
  public ExpectedRoutineInterfaceMutant() {}
  
  public ExpectedRoutineInterfaceMutant(I_ExpectedRoutineInterface other) {
    interfaceClass_ = other.getInterfaceClass();
    List<Class<?>> gts = other.getGenericTypes();
    if (gts != null) {
      if (gts.size() > 0) {
        setGenericTypes(gts);
      }
    }
    
  }
  
  @Override
  public boolean equals(Object o) {
    return equals(this, o);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_ExpectedRoutineInterface#getInterfaceClass()
   */
  @Override
  public Class<?> getInterfaceClass() {
    return interfaceClass_;
  }
  
  @Override
  public int hashCode() {
    return hashCode(this);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_ExpectedRoutineInterface#getGenericTypes()
   */
  @Override
  public List<Class<?>> getGenericTypes() {
    if (genericTypes_ == null) {
      return Collections.emptyList();
    }
    return genericTypes_;
  }
  public void setInterfaceClass(Class<?> interfaceClass) {
    interfaceClass_ = interfaceClass;
  }
  public void setGenericTypes(List<Class<?>> genericTypes) {
    if (genericTypes_ == null) {
      genericTypes_ = new ArrayList<Class<?>>();
    }
    genericTypes_.clear();
    if (genericTypes != null) {
      for (Class<?> inter: genericTypes) {
        genericTypes_.add(inter);
      }
    }
  }

  
}
