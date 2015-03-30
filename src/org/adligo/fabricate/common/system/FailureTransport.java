package org.adligo.fabricate.common.system;

import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

public class FailureTransport implements I_FailureTransport {
  private final boolean logged_;
  private final FailureType failure_;
  
  public FailureTransport(boolean logged, FailureType failure) {
    logged_ = logged;
    if (failure == null) {
      throw new IllegalArgumentException("failure");
    }
    failure_ = failure;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_FailureTransport#isLogged()
   */
  @Override
  public boolean isLogged() {
    return logged_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_FailureTransport#getFailure()
   */
  @Override
  public FailureType getFailure() {
    return failure_;
  }
}
