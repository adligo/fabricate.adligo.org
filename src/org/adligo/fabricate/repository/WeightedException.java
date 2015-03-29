package org.adligo.fabricate.repository;

/**
 * This class is used to keep track of Dependency download issues
 * noting the most successful download, in the final message to the
 * user by weight;
 * @author scott
 *
 */
public class WeightedException {
  private final Exception exception_;
  private final int weight_;
  
  public WeightedException(Exception x, int weight) {
    exception_ = x;
    weight_ = weight;
  }

  public Exception getException() {
    return exception_;
  }

  public int getWeight() {
    return weight_;
  }
}
