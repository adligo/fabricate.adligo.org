package org.adligo.fabricate.routines;

public interface I_InputAware<T> extends I_GenericTypeAware {
  public void setInput(T input);
}
