package org.adligo.fabricate.external;

import java.io.File;
import java.io.IOException;

public class JavaCalls {
  private static Executor EXE = Executor.INSTANCE;
  public static final JavaCalls INSTANCE = new JavaCalls();
  
  private JavaCalls() {}
  
  public String getJavaHome() throws IllegalStateException {
    String home = System.getenv("JAVA_HOME");
    if (home == null) {
      throw new IllegalStateException();
    }
    return home;
  }
  
  public String getJavaVersion(String homeDir, String seperator) throws IOException, InterruptedException {
    String version = EXE.executeProcess(new File("."), 
        homeDir + seperator + "bin" + seperator + "java", "-version");
    
    char [] chars = version.toCharArray();
    StringBuilder sb = new StringBuilder();
    
    boolean inDoubleQuotes = false;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '"') {
        if (!inDoubleQuotes) {
          inDoubleQuotes = true;
        } else {
          inDoubleQuotes = false;
        }
      } else if (inDoubleQuotes) {
        sb.append(c);
      }
    }
    
    String versionNbrString = sb.toString();
    return versionNbrString;
  } 
  
  public double getJavaMajorVersion(String javaVersion) {
   
    char [] chars = javaVersion.toCharArray();
    int dots = 0;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '.') {
        dots++;
        if (dots >= 2) {
          break;
        } else {
          sb.append(c);
        }
      } else {
        sb.append(c);
      }
    }
    String majorVersion = sb.toString();
    return Double.parseDouble(majorVersion);
  }
}
