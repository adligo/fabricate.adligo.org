package org.adligo.fabricate.routines;

public interface I_OutputProducer<T> extends I_GenericTypeAware {
  public T getOutput();
}
