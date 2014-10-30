package org.adligo.fabricate.external;

import java.io.File;
import java.io.IOException;

public class JavaCalls {

  public static String getJavaVersion() throws IOException, InterruptedException {
    return getJavaVersion(new File("."));
  }
  
  public static String getJavaVersion(File dir) throws IOException, InterruptedException {
    String version = Executor.executeProcess(dir, "java", "-version");
    
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
  
  public static double getJavaMajorVersion(String javaVersion) {
   
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
