package org.adligo.fabricate.common.files;

import java.io.IOException;

public class DefaultIOCloseTracker implements I_IOCloseTracker {
  public static final DefaultIOCloseTracker INSTANCE = new DefaultIOCloseTracker();
  
  private DefaultIOCloseTracker() {}
  
  @Override
  public void onCloseException(IOException x) {
    //do nothing with the close exception
  }

}
