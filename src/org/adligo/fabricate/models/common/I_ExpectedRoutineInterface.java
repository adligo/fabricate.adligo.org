package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_ExpectedRoutineInterface {

  public abstract Class<?> getInterfaceClass();

  public abstract List<Class<?>> getGenericTypes();

}