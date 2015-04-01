package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.I_ExecutionEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class GitCalls implements I_GitCalls {
  private final I_FabSystem sys_;
  private final I_SystemMessages messages_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final ExecutorService service_;
  
  private String hostname_;
  private String protocol_;
  private int port_;
  private String remotePath_;
  private String user_;
  
  
  public GitCalls(I_FabSystem sys) {
    sys_ = sys;
    log_ = sys.getLog();
    files_ = sys.getFileIO();
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
  public void checkout(String project, String localProjectDir, String version) throws IOException {
    
    I_Executor exe = sys_.getExecutor();
    String inDir = localProjectDir + files_.getNameSeparator() + project;
    
    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV, inDir, 
        "git", "checkout", version);
    String result = er.getOutput();
    
    if (isSuccess(result)) {
      return;
    } else {
      throw new IOException(result);
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#pull(java.lang.String, java.lang.String)
   */
  @Override
  public I_ExecutingProcess pull(I_ExecutionEnvironment env, String project, String localProjectDir) throws IOException {
    I_Executor exe = sys_.getExecutor();
    String inDir = localProjectDir + files_.getNameSeparator() + project;
    I_ExecutingProcess er = exe.startProcess(env, service_, inDir, 
        "git", "pull");
    return er;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#isSuccess(java.lang.String result)
   */
  public boolean isSuccess(String result) {
    if (StringUtils.isEmpty(result)) {
      return false;
    }
    if (result.indexOf("fatal:") == -1 && result.indexOf("error:") == -1) {
      return true;
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#isSuccess(org.adligo.fabricate.common.I_ExecutingProcess process)
   */
  public boolean isSuccess(I_ExecutingProcess process) {
    List<String> out = process.getOutput();
    if (out == null | out.size() == 0) {
      return true;
    }
    for (String line: out) {
      if (!isSuccess(line)) {
        return false;
      }
    }
    return true;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#check(org.adligo.fabricate.common.system.I_Executor)
   */
  @Override
  public void check(I_Executor exe) throws IOException {

    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
        ".", "git", "--version");
    String result = er.getOutput();
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
          throw new IOException(message + sys_.lineSeparator() + result);
        }
        return;
      } else {
        throw new IOException(result);
      }
    }
    throw new IOException(result);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#describe()
   */
  @Override
  public String describeVersion() throws IOException {
    return describeVersion(".");
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_GitCalls#describe(java.lang.String)
   */
  @Override
  public String describeVersion(String where) throws IOException {
    
    String absPath = files_.getAbsolutePath(where);
    
    String head = files_.readFile(absPath + ".git" + files_.getNameSeparator() + "HEAD");
    if (StringUtils.isEmpty(head)) {
      return "snapshot";
    }
    head = head.trim();
    char [] chars = head.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == ' ' || c == ':' || c == '/') {
        //.git/HEAD contains something like 'ref: refs/heads/master'
        //also note / is used on windows and linux.
        return "snapshot";
      }
    }
    I_Executor exe = sys_.getExecutor();
    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
        where, "git", "describe", "--exact-match", head);
    String result = er.getOutput();
    if (StringUtils.isEmpty(result)) {
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
   * @see org.adligo.fabricate.common.system.I_GitCalls#status(java.lang.String)
   */
  @Override
  public List<String> status(String where) throws IOException {
    
    I_Executor exe = sys_.getExecutor();
    I_ExecutingProcess process_ = exe.startProcess(FabricationMemoryConstants.EMPTY_ENV,
        service_, where, "git", "status");
    List<String> ret = new ArrayList<String>();
    while (!process_.isFinished()) {
      try {
        process_.waitUntilFinished(1000);
        List<String> output = process_.getOutput();
        ret.addAll(output);
      } catch (InterruptedException e) {
        sys_.currentThread().interrupt();
      }
    }
    return ret;
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
