package org.adligo.fabricate.external;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class GitCalls {
  private static PrintStream OUT = System.out;
  private I_FabContext ctx_;
  private String hostname_;
  private String user_;
  private String remotePath_;
  
  public boolean clone(String project, String localProjectDir) throws IOException {
    String result;
    try {
      String command = null;
      if (StringUtils.isEmpty(user_)) {
        command = hostname_ + ":" + remotePath_ + project;
      } else {
        command = user_ + "@" + hostname_ + ":" + remotePath_ + project;
      }
     result = Executor.executeProcess(new File(localProjectDir), "git", "clone", command);
      if (ctx_.isLogEnabled(GitCalls.class)) {
        OUT.println("In " + localProjectDir + System.lineSeparator() +
            "git clone " + command + System.lineSeparator() +
            "finished with the following output " + System.lineSeparator() +
            result + System.lineSeparator());
      }
    } catch (InterruptedException e) {
      throw new IOException(e);
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
    String result;
    try {
      result = Executor.executeProcess(new File(localProjectDir + File.separator + project), 
          "git", "checkout", version);
      if (ctx_.isLogEnabled(GitCalls.class)) {
        OUT.println("In " + localProjectDir + File.separator + project + System.lineSeparator() +
            "git checkout " + version + System.lineSeparator() +
            "finished with the following output " + System.lineSeparator() +
            result + System.lineSeparator());
      }
    } catch (InterruptedException e) {
      throw new IOException(e);
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
    String result;
    try {
     result = Executor.executeProcess(new File(localProjectDir + File.separator + project), 
          "git", "pull", "-f","origin","master");
      if (ctx_.isLogEnabled(GitCalls.class)) {
        OUT.println("In " + localProjectDir + File.separator + project + System.lineSeparator() +
            "git checkout origin master" + System.lineSeparator() +
            "finished with the following output " + System.lineSeparator() +
            result + System.lineSeparator());
      }
    } catch (InterruptedException e) {
      throw new IOException(e);
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
  
  public static boolean check() throws IOException {
    String result;
    try {
      result = Executor.executeProcess(new File("."), "git", "--version");
    } catch (InterruptedException e) {
      throw new IOException(e);
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
  
  public static String describe() {
    return describe(".");
  }
  public static String describe(String where) {
    String result = null;
    try {
      result = Executor.executeProcess(new File(where), "git", "describe");
    } catch (InterruptedException | IOException e) {
      //do nothing it has a exit code of 128 when there are no tags.
    }
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

  public I_FabContext getCtx() {
    return ctx_;
  }

  public void setCtx(I_FabContext ctx) {
    ctx_ = ctx;
  }
}
