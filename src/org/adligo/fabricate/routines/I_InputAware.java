package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;

public interface I_InputAware<T> extends I_GenericTypeAware, I_FabricationRoutine {
  public void setInput(T input);
}
