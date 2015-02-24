package org.adligo.fabricate.routines;

import java.math.BigInteger;

/**
 * This interface is used to aggregate coverage information 
 * for a stage which tests, or for a task that tests a project.
 * @author scott
 *
 */
public interface I_CoverageAware {
  /**
   * The total number of coverage units,
   * for jacoco implementations this is the same as probes.
   * @return
   */
  public BigInteger getUnits();
  
  /**
   * The total number of coverage units that 
   * were covered by the run of the tests.
   * For jacoco implementations this is the number
   * of probes that are true.
   * @return
   */
  public BigInteger getCovered();
}
