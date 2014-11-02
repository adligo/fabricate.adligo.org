package org.adligo.fabricate.common;


/**
 * I_FabTask is a interface to allow plug-able
 * tasks to be called from fabricate.  It is generally
 * assumed that I_FabTask's are NOT concurrent
 * and only one at a time will run on the fabricate
 * environment.
 * The run method should be thread safe since
 * it will be called by multiple threads, any memory
 * should also use the concurrent collections.
 * 
 * @author scott
 *
 */
public interface I_FabStage extends Runnable {
  public void setStageName(String stageName);
  public void setup(I_FabContext ctx);
  public boolean isConcurrent();
  /**
   * The thread that calls this method
   * will be blocked until it is notified
   * that it is ok to continue
   */
  public void waitUntilFinished();
  public boolean hadException();
  public Exception getException();
}
