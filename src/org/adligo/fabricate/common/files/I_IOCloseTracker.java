package org.adligo.fabricate.common.files;

import java.io.IOException;

/**
 * This interface helps tests track
 * IOExceptions that are thrown from streams, or channels
 * which throw a IOException on close.
 * @author scott
 *
 */
public interface I_IOCloseTracker {
  public void onCloseException(IOException x);
}
