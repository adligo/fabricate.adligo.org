package org.adligo.fabricate.models.scm;


public interface I_GitScm extends I_Scm {
  public String getHost();
  public String getPath();
  public String getUsername();
  public String getProtocol();
  
}
