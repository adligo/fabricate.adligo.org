package org.adligo.fabricate.routines;

public interface I_ResultFactory<T> extends I_GenericTypeAware {
  public T getResult();
}
