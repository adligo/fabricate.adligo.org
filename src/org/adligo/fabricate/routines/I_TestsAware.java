package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.math.BigInteger;

/**
 * This interface is used to track the aggregate number of
 * tests run by a stage on multiple projects, and on
 * a task on a single project.
 * @author scott
 *
 */
public interface I_TestsAware extends I_FabricationRoutine {
  /**
   * The total number of tests (may include ignored tests and any other tests).
   * For junit and tests4j this is the number of @Test methods.
   * @return
   */
  public BigInteger getCount();
  /**
   * The number of tests that passed.
   * @return
   */
  public BigInteger getPassed();
  /**
   * The number of tests that failed.
   * @return
   */
  public BigInteger getFailed();
}
