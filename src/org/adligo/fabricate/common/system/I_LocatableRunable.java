package org.adligo.fabricate.common.system;

/**
 * This is a runnable that helps locate what it is currently doing.
 * @author scott
 *
 */
public interface I_LocatableRunable extends Runnable {
  /**
   * This should user readable information about
   * where the routine is currently executing.
   * I_SystemMessages has constants to help;
   * Some examples;
   * Command encrypt is still setting up.
   * Command encrypt is still running.
   * Command foo task bar is still running.
   * Command classpath2Eclipse is still running on project nutz.example.com.
   * Command foo task bar is still running on project nutz.example.com.
   * 
   * Build stage compileAndJar is still running on project nutz.example.com.
   * Share stage submit2intelligence4j is still running.
   * @return
   */
  public String getCurrentLocation();
}
