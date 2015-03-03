package org.adligo.fabricate.common.system;

import java.util.List;

/**
 * This interface represents 
 * a java.lang.Process executing on another thread.
 * 
 * @author scott
 *
 */
public interface I_ExecutingProcess extends Runnable {

  /**
   * kills of the executing sub process.
   */
  public void destroy() ;
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
  public abstract Runnable getDelegate();
  
  /**
   * the process exit code
   * @return
   */
  public int getExitCode();
  /**
   * Wait for the runnable to finish running for a specific number of milliseconds.
   * @param millis
   * @throws InterruptedException
   */
  public abstract void waitUntilFinished(long millis) throws InterruptedException;
  /**
   * Returns a list of lines the executing process has 
   * executed so far.  Each call to this method 
   * should return only the lines of output since the
   * last call.
   * @return
   */
  public List<String> getOutput();
  
  /**
   * write input into the executing process 
   * (i.e. the git open ssh keystore password for the local keystore).
   * @param input
   */
  public void writeInputToProcess(String input, String charSet);
}
