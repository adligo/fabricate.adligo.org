package org.adligo.fabricate.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Executor {
  public static String executeProcess(File inDir, String ... args) throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder(args);
    pb.redirectErrorStream(true);
    
    if (inDir != null) {
      pb.directory(inDir);
    }
    
    Process p = pb.start();
    InputStream is = null;
    InputStreamReader isr = null;
      BufferedReader br = null;
      
      StringBuilder sb = new StringBuilder();
    try {
      is = p.getInputStream();
      isr = new InputStreamReader(is);
      br = new BufferedReader(isr);
      String line;
        while ((line = br.readLine()) != null) {
          sb.append(line);
        }
    } catch (IOException x) {
      //eat
    } finally {
      if (br != null) {
        br.close();
      }
      if (isr != null) {
        isr.close();
      }
      if (is != null) {
        is.close();
      }
    }
    
    return sb.toString();
  }
}
