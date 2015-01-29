package org.adligo.fabricate.common.system;

import java.io.IOException;

/**
 * Implementations of this interface are expected to be
 * thread safe with, multiple threads executing through them.
 * @author scott
 *
 */
public interface I_Executor {

  /**
   * This method executes a external process in a manor that
   * is similar to the way a script would execute a command line
   * command (i.e. git, ls, dir, cd).  Note since external software
   * may return strange exit codes, the client of this method
   * must deal with the exitCode, usually this is something like;<br/>
   * <pre><code>
   * I_Executor exe;
   * I_ExecutionResult er = exe.executeProcess(".","ls");
   * if (er.getExitCode() != 0) {
   *    throw new IOException(er.getOutput());
   * }
   * </code></pre>
   * @param inDir
   * @param args
   * @return
   * @throws IOException when there was a IOException reading the output 
   * of the process.
   */
  public abstract I_ExecutionResult executeProcess(String inDir, String... args) throws IOException;

}