package org.adligo.fabricate.common.system;

import java.io.File;
import java.io.IOException;

/**
 * This class allows Mockito to create a mock for stubbing.
 * @author scott
 *
 */
public class ProcessBuilderWrapper {
  private ProcessBuilder delegate_;
  
  public ProcessBuilderWrapper(ProcessBuilder pb) {
    delegate_ = pb;
  }

  public ProcessBuilderWrapper redirectErrorStream(boolean redirectErrorStream) {
    delegate_.redirectErrorStream(redirectErrorStream);
    return this;
  }

  public Process start() throws IOException {
    return delegate_.start();
  }
  
  public ProcessBuilder getDelegate() {
    return delegate_;
  }

  public ProcessBuilderWrapper directory(File directory) {
    delegate_.directory(directory);
    return this;
  }
}
