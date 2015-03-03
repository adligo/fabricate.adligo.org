package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.I_ExecutionEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class GitCalls implements I_GitCalls {
  private final I_FabSystem sys_;
  private final I_SystemMessages messages_;
  private final I_FabLog log_;
  private final ExecutorService service_;
  
  private String hostname_;
  private String protocol_;
  private int port_;
  private String remotePath_;
  private String user_;
  
  
  public GitCalls(I_FabSystem sys) {
    sys_ = sys;
    log_ = sys.getLog();
    I_FabricateConstants constants = sys_.getConstants();
    messages_ = constants.getSystemMessages();
    service_ = sys.newFixedThreadPool(1);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#clone(java.lang.String, java.lang.String)
   */
  @Override
  public I_ExecutingProcess clone(I_ExecutionEnvironment env, String project, String localProjectDir) throws IOException {
    String url = protocol_ + "://";
    if ( !StringUtils.isEmpty(user_)) {
      url = url + user_ + "@";      
    } 

    url = url + hostname_ + ":" + port_ + remotePath_ + project;
    I_Executor exe = sys_.getExecutor();
    I_ExecutingProcess er = exe.startProcess(env, service_, localProjectDir, 
        "git", "clone", url);
    return er;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#checkout(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public boolean checkout(String project, String localProjectDir, String version) throws IOException {
    
    I_Executor exe = sys_.getExecutor();
    String inDir = localProjectDir + File.separator + project;
    
    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV, inDir, 
        "git", "checkout", version);
    String result = er.getOutput();
    
    if (result != null && result.trim().length() >= 1) {
      if (isSuccess(result)) {
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#pull(java.lang.String, java.lang.String)
   */
  @Override
  public boolean pull(I_ExecutionEnvironment env, String project, String localProjectDir) throws IOException {
    
    String message = messages_.getStartingGetPullOnProjectX();
    message = message.replace("<X/>", project);
    log_.println(message);
    
    I_Executor exe = sys_.getExecutor();
    String inDir = localProjectDir + File.separator + project;
    I_ExecutionResult er = exe.executeProcess(env, inDir, 
        "git", "pull");
    String result = er.getOutput();
   
    message = messages_.getFinishedGetPullOnProjectX();
    message = message.replace("<X/>", project);
    log_.println(message);
    
    if (result != null && result.trim().length() >= 1) {
      if (isSuccess(result)) {
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }
  
  public boolean isSuccess(String result) {
    if (result.indexOf("fatal:") == -1 && result.indexOf("error:") == -1) {
      return true;
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#check(org.adligo.fabricate.common.system.I_Executor)
   */
  @Override
  public boolean check(I_Executor exe) throws IOException {

    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
        ".", "git", "--version");
    String result = er.getOutput();
    System.out.println("GitCalls " + result);
    if (result != null && result.trim().length() >= 1) {
      if (isSuccess(result)) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbMinor = new StringBuilder();
        char [] chars = result.toCharArray();
        boolean inBrackets = false;
        int dots = 0;
        for (int i = 0; i < chars.length; i++) {
          char c = chars[i];
          if (c == '(') {
            inBrackets = true;
          } else if (inBrackets) {
            if (c == ')') {
              inBrackets = false;
            }
          }
          if (!inBrackets && (Character.isDigit(c) || c == '.')) {
            if (c ==  '.') {
              dots++;
            }
            if (dots < 2) {
              sb.append(c);
            } else {
              if (c == '.') {
                if (sbMinor.toString().length() != 0) {
                  sbMinor.append(c);
                }
              } else {
                sbMinor.append(c);
              }
            }
          }
        }
        Double majorVersion = new Double(sb.toString());
        Double minorVersion = new Double(sbMinor.toString());
        if (majorVersion.doubleValue() < 1.9 || minorVersion.doubleValue() < 3.0 ) {
          String message = messages_.getThisVersionOfFabricateRequiresGitXOrGreater();
          message = message.replace("<X/>", "1.9.3");
          throw new IOException(message + sys_.lineSeperator() + result);
        }
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#describe()
   */
  @Override
  public String describe() throws IOException {
    return describe(".");
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#describe(java.lang.String)
   */
  @Override
  public String describe(String where) throws IOException {
    
    I_Executor exe = sys_.getExecutor();
    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
        where, "git", "describe");
    String result = er.getOutput();
    if (result == null) {
      return "snapshot";
    }
    if (result.indexOf("fatal:") != -1) {
      return "snapshot";
    }
    if (isSuccess(result)) {
      return result;
    } else {
      return "snapshot";
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#getHostname()
   */
  @Override
  public String getHostname() {
    return hostname_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#setHostname(java.lang.String)
   */
  @Override
  public void setHostname(String hostname) {
    hostname_ = hostname;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#getUser()
   */
  @Override
  public String getUser() {
    return user_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#setUser(java.lang.String)
   */
  @Override
  public void setUser(String user) {
    user_ = user;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#getRemotePath()
   */
  @Override
  public String getRemotePath() {
    return remotePath_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#setRemotePath(java.lang.String)
   */
  @Override
  public void setRemotePath(String remotePath) {
    remotePath_ = remotePath;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#getProtocol()
   */
  @Override
  public String getProtocol() {
    return protocol_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#setProtocol(java.lang.String)
   */
  @Override
  public void setProtocol(String protocol) {
    this.protocol_ = protocol;
  }


  public int getPort() {
    return port_;
  }


  public void setPort(int port) {
    this.port_ = port;
  }

}
