package org.adligo.fabricate.common.system;

import org.adligo.fabricate.models.common.I_ExecutionEnvironment;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

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
   * 
   * Note this method should only be used for short processes (less than 3 seconds)
   * for longer processes use startProcess.
   * @param inDir
   * @param args
   * @return
   * @throws IOException when there was a IOException reading the output 
   * of the process.
   */
  public abstract I_ExecutionResult executeProcess(
      I_ExecutionEnvironment env, String inDir, String... args) throws IOException;

  /**
   * This method is similar to executeProcess, 
   * however it is designed for longer running processes.
   * @param service a single thread executor service.
   * @param inDir
   * @param args
   * @return
   * @throws IOException
   */
  public abstract I_ExecutingProcess startProcess(
      I_ExecutionEnvironment env, ExecutorService service, String inDir, String... args) throws IOException;
}