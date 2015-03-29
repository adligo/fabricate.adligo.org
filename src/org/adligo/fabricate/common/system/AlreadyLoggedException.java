package org.adligo.fabricate.common.system;

/**
 * This exception simply signifies that 
 * a stack trace has already been logged, 
 * so another trace shouldn't be logged for this exception.
 * @author scott
 *
 */
public class AlreadyLoggedException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public AlreadyLoggedException(Exception x) {
    super(x);
  }
}
