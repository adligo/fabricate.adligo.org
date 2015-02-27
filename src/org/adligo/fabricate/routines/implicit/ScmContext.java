package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.I_RoutineBrief;

import java.util.List;

public class ScmContext {
  /**
   * key to the fabricate.xml scm parameter
   * for the open ssl keystorePassword
   * which must be encrypted in the file 
   * using the encrypt, decrypt trait set in fabricate.xml 
   * (or the implicit one).
   */
  public static final String KEYSTORE_PASSWORD = "keystorePassword";
  /**
   * key to the fabricate.xml scm parameter
   * for the git protocol one of (https, ssh)
   */
  public static final String PROTOCOL = "protocol";
  /**
  * key to the fabricate.xml scm parameter
  * for the git server path
  * (i.e. /opt/git/org)
  */
  public static final String PATH = "path";
  /**
   * key to the fabricate.xml scm parameter
   * for the git server hostname
   * (i.e. github.com)
   */
  public static final String HOSTNAME = "hostname";
  private final String hostname_;
  private final String path_;
  private final String protocol_;
  private final String keystorePassword_;
  
  public ScmContext(I_RoutineBrief routine, String keystorePassword) {
    List<String> hostnames = routine.getParameters(HOSTNAME);
    if (hostnames == null || hostnames.size() < 1) {
      throw new IllegalArgumentException(HOSTNAME);
    }
    hostname_ = hostnames.get(0);
    if (StringUtils.isEmpty(hostname_)) {
      throw new IllegalArgumentException(HOSTNAME);
    }
    
    List<String> paths = routine.getParameters(PATH);
    if (paths == null || paths.size() < 1) {
      throw new IllegalArgumentException(PATH);
    }
    path_ = paths.get(0);
    if (StringUtils.isEmpty(path_)) {
      throw new IllegalArgumentException(PATH);
    }
    
    List<String> protocols = routine.getParameters(PROTOCOL);
    if (paths == null || paths.size() < 1) {
      protocol_ = "https";
    } else {
      String protocol = protocols.get(0);
      if (StringUtils.isEmpty(protocol)) {
        protocol_ = "https"; 
      } else {
        protocol_ = protocol;
      }
    }
    //may be empty for ssh without a keystore password
    keystorePassword_ = keystorePassword;
  }
  
  public String getHostname() {
    return hostname_;
  }
  public String getPath() {
    return path_;
  }
  public String getProtocol() {
    return protocol_;
  }
  public String getKeystorePassword() {
    return keystorePassword_;
  }
  
  /*
  <cns:param key="hostname" value="git" />
  <cns:param key="path" value="/opt/git/org" />
  <cns:param key="protocol" value="ssh" />
  */
}
