package org.adligo.fabricate.common.system;

import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

public class FailureTransport {
  private final boolean logged_;
  private final FailureType failure_;
  
  public FailureTransport(boolean logged, FailureType failure) {
    logged_ = logged;
    if (failure == null) {
      throw new IllegalArgumentException("failure");
    }
    failure_ = failure;
  }

  public boolean isLogged() {
    return logged_;
  }

  public FailureType getFailure() {
    return failure_;
  }
}
