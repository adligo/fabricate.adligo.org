package org.adligo.fabricate.common.system;

/**
 * A implementation of I_ExecutionResult.
 * @see I_ExecutionResult
 * @author scott
 *
 */
public class ExecutionResultMutant implements I_ExecutionResult {
  private String output_;
  private int exitCode_;
  
  public String getOutput() {
    return output_;
  }
  public int getExitCode() {
    return exitCode_;
  }
  
  public void setOutput(String output) {
    this.output_ = output;
  }
  public void setExitCode(int exitCode) {
    this.exitCode_ = exitCode;
  }
  
}
