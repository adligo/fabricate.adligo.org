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
public interface I_FabTask extends Runnable {
  public void setStageName(String stageName);
  public void setup(I_FabContext ctx);
  public boolean isConcurrent();
  public boolean isFinished();
  public boolean hadException();
  public Exception getException();
}
