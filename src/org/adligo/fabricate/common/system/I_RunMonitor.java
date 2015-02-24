package org.adligo.fabricate.common.system;

/**
 * A wrapper around Runnable that 
 * calls run on the current thread checking
 * for a Throwable thrown from the run method.
 * @author scott
 *
 */
public interface I_RunMonitor extends Runnable {

  /**
   * If the delegate run method threw a Throwable.
   * @return
   */
  public boolean hasFailure();
  /**
   * If the delegate has exited the run method.
   * @return
   */
  public abstract boolean isFinished();

  
  /**
   * The Throwable thrown by the delegates run method.
   * @return
   */
  public abstract Throwable getCaught();

  /**
   * The delegate runnable.
   * @return
   */
  public abstract I_LocatableRunable getDelegate();
  
  /**
   * Which runnable of this type, run location (command, stage).
   * @return
   */
  public abstract int getSequence();

  /**
   * Wait for the runnable to finish running for a specific number of milliseconds.
   * @param millis
   * @throws InterruptedException
   */
  public abstract void waitUntilFinished(long millis) throws InterruptedException;

  
}