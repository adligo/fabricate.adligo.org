package org.adligo.fabricate.common.system;

import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

public interface I_FailureTransport {

  public abstract boolean isLogged();

  public abstract FailureType getFailure();

}