package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.I_RoutineBrief;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ScmContext {


  /**
  * key to the fabricate.xml scm parameter
  * for the git server path
  * (i.e. /opt/git/org)
  */
  public static final String PATH = "path";
  /**
   * key to the fabricate.xml scm parameter
   * for the git protocol one of (https, ssh)
   */
  public static final String PROTOCOL = "protocol";
  /**
   * key to the fabricate.xml scm parameter
   * for the git port must be a positive integer.
   */
  public static final String PORT = "port";
  /**
   * key to the fabricate.xml scm parameter
   * for the git server hostname
   * (i.e. github.com)
   */
  public static final String HOSTNAME = "hostname";
  /**
   * key to the fabricate.xml scm paramter
   * for the git server username.
   */
  public static final String USER = "user";
  private static final Set<String> KNOWN_PROTOCOLS = getKnownProtocols();
  
  private static Set<String> getKnownProtocols() {
    Set<String> toRet = new HashSet<String>();
    toRet.add("ssh");
    toRet.add("https");
    return Collections.unmodifiableSet(toRet);
  }
  
  private final String hostname_;
  private final String path_;
  private final String protocol_;
  private final int port_;
  private final String username_;
  
  public ScmContext(I_RoutineBrief routine) {
   
    hostname_ = routine.getParameter(HOSTNAME);
    if (StringUtils.isEmpty(hostname_)) {
      throw new IllegalArgumentException(HOSTNAME);
    }
    
    String path = routine.getParameter(PATH);
    if (StringUtils.isEmpty(path)) {
      throw new IllegalArgumentException(PATH);
    }
    //fix the path for lazy usage
    if (path.charAt(path.length() - 1) != '/') {
        path = path + "/";
    } 
    if (path.charAt(0) != '/') {
      path = "/" + path;
    } 
    path_ = path;
    
    String protocol = routine.getParameter(PROTOCOL);
    if (StringUtils.isEmpty(protocol)) {
      protocol_ = "https"; 
    } else {
      protocol_ = protocol.toLowerCase();
    }
    if ( !KNOWN_PROTOCOLS.contains(protocol_)) {
      throw new IllegalArgumentException(PROTOCOL);
    }
    String port = routine.getParameter(PORT);
    if (StringUtils.isEmpty(port)) {
      if ("ssh".equals(protocol_)) {
        port_ = 22;
      } else {
        port_ = 443;
      }
    } else {
      port_ = Integer.parseInt(port);
    }
    String user = routine.getParameter(USER);
    if (StringUtils.isEmpty(user)) {
      username_ = "";
    } else {
      username_ = user;
    }
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

  public String getUsername() {
    return username_;
  }

  public int getPort() {
    return port_;
  }
  
  /*
  <cns:param key="hostname" value="git" />
  <cns:param key="path" value="/opt/git/org" />
  <cns:param key="protocol" value="ssh" />
  */
}
