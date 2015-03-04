package org.adligo.fabricate.common.system;

import java.util.List;

/**
 * This interface represents a Runnable
 * that wraps a java.lang.Process allowing
 * other threads to work with the Processes IO.
 * 
 * @author scott
 *
 */
public interface I_ProcessRunnable  extends Runnable {
  public void destroy();
  public int getExitCode();
  public List<String> getOutput();
  public void writeInputToProcess(String input, String charSet);
}
