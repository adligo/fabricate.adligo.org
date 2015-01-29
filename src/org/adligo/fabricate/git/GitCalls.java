package org.adligo.fabricate.git;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class GitCalls {
  private I_FabSystem sys_;
  private I_FabLog log_;
  private String hostname_;
  private String user_;
  private String remotePath_;
  
  public GitCalls(I_FabSystem sys) {
    sys_ = sys;
  }
  
  public boolean clone(String project, String localProjectDir) throws IOException {
    String command = null;
    if (StringUtils.isEmpty(user_)) {
      command = hostname_ + ":" + remotePath_ + project;
    } else {
      command = user_ + "@" + hostname_ + ":" + remotePath_ + project;
    }
    I_Executor exe = sys_.getExecutor();
    I_ExecutionResult er = exe.executeProcess(localProjectDir, 
        "git", "clone", command);
    String result = er.getOutput();
    
    if (log_.isLogEnabled(GitCalls.class)) {
      log_.println("In " + localProjectDir + System.lineSeparator() +
          "git clone " + command + System.lineSeparator() +
          "finished with the following output " + System.lineSeparator() +
          result + System.lineSeparator());
    }
    if (result != null && result.trim().length() >= 1) {
      if (gitOutputSuccess(result)) {
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }
  
  public boolean checkout(String project, String localProjectDir, String version) throws IOException {
    
    I_Executor exe = sys_.getExecutor();
    String inDir = localProjectDir + File.separator + project;
    
    I_ExecutionResult er = exe.executeProcess(inDir, 
        "git", "checkout", version);
    String result = er.getOutput();
    
    if (log_.isLogEnabled(GitCalls.class)) {
      log_.println("In " + localProjectDir + File.separator + project + System.lineSeparator() +
          "git checkout " + version + System.lineSeparator() +
          "finished with the following output " + System.lineSeparator() +
          result + System.lineSeparator());
    }
    if (result != null && result.trim().length() >= 1) {
      if (gitOutputSuccess(result)) {
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }

  public boolean pull(String project, String localProjectDir) throws IOException {
    I_Executor exe = sys_.getExecutor();
    String inDir = localProjectDir + File.separator + project;
    I_ExecutionResult er = exe.executeProcess(inDir, 
        "git", "pull", "-f","origin","master");
    String result = er.getOutput();
    if (log_.isLogEnabled(GitCalls.class)) {
      log_.println("In " + localProjectDir + File.separator + project + System.lineSeparator() +
          "git checkout origin master" + System.lineSeparator() +
          "finished with the following output " + System.lineSeparator() +
          result + System.lineSeparator());
    }
    if (result != null && result.trim().length() >= 1) {
      if (gitOutputSuccess(result)) {
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }
  
  public static boolean gitOutputSuccess(String result) {
    if (result.indexOf("fatal:") == -1 && result.indexOf("error:") == -1) {
      return true;
    }
    return false;
  }
  
  public static boolean check(I_Executor exe) throws IOException {

    I_ExecutionResult er = exe.executeProcess(".", "git", "--version");
    String result = er.getOutput();
    
    if (result != null && result.trim().length() >= 1) {
      if (gitOutputSuccess(result)) {
        return true;
      } else {
        throw new IOException(result);
      }
    }
    return false;
  }
  
  public static String describe(I_FabSystem sys) throws IOException {
    return describe(sys, ".");
  }
  public static String describe(I_FabSystem sys, String where) throws IOException {
    
    I_Executor exe = sys.getExecutor();
    I_ExecutionResult er = exe.executeProcess(where, "git", "describe");
    String result = er.getOutput();
    if (result == null) {
      return "snapshot";
    }
    if (result.indexOf("fatal:") != -1) {
      return "snapshot";
    }
    if (gitOutputSuccess(result)) {
      return result;
    } else {
      return "snapshot";
    }
  }

  public String getHostname() {
    return hostname_;
  }

  public void setHostname(String hostname) {
    hostname_ = hostname;
  }

  public String getUser() {
    return user_;
  }

  public void setUser(String user) {
    user_ = user;
  }

  public String getRemotePath() {
    return remotePath_;
  }

  public void setRemotePath(String remotePath) {
    remotePath_ = remotePath;
  }

}
