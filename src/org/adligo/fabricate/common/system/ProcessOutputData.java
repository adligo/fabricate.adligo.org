package org.adligo.fabricate.common.system;

/**
 * This class is for writing out to a
 * executing process (i.e. send a open ssh key-store password
 * for the local machine to a git process).
 * 
 * @author scott
 *
 */
public class ProcessOutputData {
  private final String data_;
  /**
   * a null charset uses the default charset for the platform
   */
  private final String charSet_;
  
  public ProcessOutputData(String data, String charSet) {
    data_ = data;
    charSet_ = charSet;
  }

  public String getData() {
    return data_;
  }

  public String getCharSet() {
    return charSet_;
  }
}
