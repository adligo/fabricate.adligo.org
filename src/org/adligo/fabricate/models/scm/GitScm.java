package org.adligo.fabricate.models.scm;

import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.GitServerType;

public class GitScm implements I_GitScm {
  private static final String GIT = "Git";
  private String host;
  private String path;
  private String user;
  private String protocol = "ssh";
  
  public GitScm(GitServerType delegate) {
    host = delegate.getHostname();
    path = delegate.getPath();
    if ("github.com".equals(host)) {
      user = "git";
    } else {
      user = delegate.getUser();
    }
    String protcolIn = delegate.getProtocol();
    if ("https".equalsIgnoreCase(protcolIn)) {
      protocol = "https";
    } else if ("local".equalsIgnoreCase(protcolIn)) {
      protocol = "local";
    } 
  }
  @Override
  public String getHost() {
    return host;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public String getUsername() {
    return user;
  }

  @Override
  public String getProtocol() {
    return protocol;
  }
  @Override
  public String getName() {
    return GIT;
  }

}
