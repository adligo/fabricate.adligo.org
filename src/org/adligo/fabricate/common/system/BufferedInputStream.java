package org.adligo.fabricate.common.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferedInputStream {
  private final BufferedReader wrapper_;
  private final InputStreamReader reader_;
  private final InputStream delegate_;
  private boolean closedWrapper_;
  private boolean closedReader_;
  private boolean closedDelegate_;
  
  public BufferedInputStream(InputStream delegate) {
    delegate_ = delegate;
    reader_ = new InputStreamReader(delegate_);
    wrapper_ = new BufferedReader(reader_);
  }
  
  public String readLine() throws IOException {
    return wrapper_.readLine();
  }
  
  public void close() {
    try {
      wrapper_.close();
    } catch (IOException x) {
      //do nothing
    }
    closedWrapper_ = true;
    try {
      reader_.close();
    } catch (IOException x) {
      //do nothing
    }
    closedReader_ = true;
    try {
      delegate_.close();
    } catch (IOException x) {
      //do nothing
    }
    closedDelegate_ = true;
  }

  public boolean isClosedWrapper() {
    return closedWrapper_;
  }

  public boolean isClosedReader() {
    return closedReader_;
  }

  public boolean isClosedDelegate() {
    return closedDelegate_;
  }

  public InputStream getDelegate() {
    return delegate_;
  }

  
}
