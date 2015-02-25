package org.adligo.fabricate.common.log;

public interface I_FabFileLog extends I_Print {
  /**
   * Close the underlying output stream,
   * ignore IOException from delegate.close().
   */
  public void close();
}
