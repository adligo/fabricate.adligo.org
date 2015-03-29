package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

public class DependencyNotAvailableException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = -5557407966833930386L;
  private I_Dependency dependency_;
  
  public DependencyNotAvailableException(I_Dependency dep) {
    dependency_ = dep;
  }

  public DependencyNotAvailableException(I_Dependency dep, String message) {
    super(message);
    dependency_ = dep;
  }

  public DependencyNotAvailableException(I_Dependency dep, String message, Exception x) {
    super(message, x);
    dependency_ = dep;
  }
  
  public I_Dependency getDependency() {
    return dependency_;
  }
  
}
