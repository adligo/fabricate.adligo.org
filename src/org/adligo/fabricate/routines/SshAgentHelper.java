package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.util.StringUtils;

/**
 * This class parses the output of the ssh-agent
 * program so it can pass the values to subsequent
 * git processes.
 * @author scott
 *
 */
public class SshAgentHelper {
  public static final String SSH_AGENT_PID = "SSH_AGENT_PID";
  public static final String SSH_AUTH_SOCK = "SSH_AUTH_SOCK";
  private String sock_;
  private String pid_;
  public SshAgentHelper(String sshAgentOut, I_SystemMessages messages) {
    StringBuilder kb = new StringBuilder();
    StringBuilder valBuilder = new StringBuilder();
    
    char [] chars = sshAgentOut.toCharArray();
    boolean pastEquals = false;
    boolean pastSemiColon = false;
    boolean lastCharLineEnd = false;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (!pastEquals) {
        if (c == '=') {
          pastEquals = true;
        } else {
          kb.append(c);
        }
      } else {
        if (pastSemiColon) {
          if (!lastCharLineEnd) {
            if (c == '\n' || c == '\r') {
              lastCharLineEnd = true;
            }
          } else {
            lastCharLineEnd = false;
            pastEquals = false;
            pastSemiColon = false;
            identifyEnvVar(kb, valBuilder);
            kb = new StringBuilder();
            valBuilder = new StringBuilder();
            if (c != '\n' && c != '\r') {
              kb.append(c);
            }
          }
        } else {
          if (c == ';') {
            pastSemiColon = true;
          } else {
            valBuilder.append(c);
          }
        }
      }
    }
    identifyEnvVar(kb, valBuilder);
    if (StringUtils.isEmpty(sock_)) {
      String message = messages.getExceptionUnableToFindSSH_AUTH_SOCKWhenParsingOutputOfSshAgent();
      throw new IllegalArgumentException(message);
    }
    if (StringUtils.isEmpty(pid_)) {
      String message = messages.getExceptionUnableToFindSSH_AGENT_PIDWhenParsingOutputOfSshAgent();
      throw new IllegalArgumentException(message);
    }
  }
  
  public void identifyEnvVar(StringBuilder kb, StringBuilder valBuilder) {
    String key = kb.toString();
    String val = valBuilder.toString();
    if (SSH_AUTH_SOCK.equals(key)) {
      sock_ = val;
    } else if (SSH_AGENT_PID.equals(key)) {
      pid_ = val;
    }
  }
  
  public String getSock() {
    return sock_;
  }
  public String getPid() {
    return pid_;
  }
}
