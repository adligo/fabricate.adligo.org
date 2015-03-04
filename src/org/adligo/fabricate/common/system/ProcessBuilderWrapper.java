package org.adligo.fabricate.common.system;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class allows Mockito to create a mock for stubbing.
 * @author scott
 *
 */
public class ProcessBuilderWrapper implements I_ProcessBuilderWrapper {
  private ProcessBuilder delegate_;
  
  public ProcessBuilderWrapper(ProcessBuilder pb) {
    delegate_ = pb;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_ProcessBuilderWrapper#directory(java.io.File)
   */
  @Override
  public I_ProcessBuilderWrapper directory(File directory) {
    delegate_.directory(directory);
    return this;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_ProcessBuilderWrapper#environment()
   */
  @Override
  public Map<String, String> environment() {
    return delegate_.environment();
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_ProcessBuilderWrapper#getDelegate()
   */
  @Override
  public ProcessBuilder getDelegate() {
    return delegate_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_ProcessBuilderWrapper#redirectErrorStream(boolean)
   */
  @Override
  public I_ProcessBuilderWrapper redirectErrorStream(boolean redirectErrorStream) {
    delegate_.redirectErrorStream(redirectErrorStream);
    return this;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_ProcessBuilderWrapper#start()
   */
  @Override
  public Process start() throws IOException {
    return delegate_.start();
  }
}
