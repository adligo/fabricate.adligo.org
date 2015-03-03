package org.adligo.fabricate.java;

import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;

import java.io.IOException;
import java.util.Collections;

public class JavaCalls {
  private final I_FabSystem sys_;
  
  public JavaCalls(I_FabSystem sys) {
    sys_ = sys;
  }
  
  public String getJavaVersion(String homeDir, String seperator) throws IOException, InterruptedException {
    I_Executor exe = sys_.getExecutor();
    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,  ".", 
        homeDir + seperator + "bin" + seperator + "java", "-version");
    String version = er.getOutput();
    
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
