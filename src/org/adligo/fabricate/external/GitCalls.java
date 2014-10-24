package org.adligo.fabricate.external;

import java.io.File;
import java.io.IOException;

public class GitCalls {

  public static boolean check() throws IOException, InterruptedException {
    String result = Executor.executeProcess(new File("."), "git", "--version");
    if (result != null && result.trim().length() >= 1) {
      return true;
    }
    return false;
  }
  
  public static String describe() throws IOException, InterruptedException {
    String result = Executor.executeProcess(new File("."), "git", "describe");
    if (result != null && result.trim().length() >= 1) {
      return "snapshot";
    }
    if (result.indexOf("fatal:") != -1) {
      return "snapshot";
    }
    return result;
  }
}
