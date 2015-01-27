package org.adligo.fabricate.external;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

/**
 * This is suppose to execute a process similar to a 
 * command line and return all the output as a string.
 * There is currently a bug where it only returns the 
 * first line of the process output, I have tried it
 * many different ways, I am guessing that the 
 * end of line character is being sent to the java
 * input stream, so a read of it only gets the first line.
 * 
 * This could be a bug in my code, the mac JVM, or the java language.
 * @author scott
 *
 */
public class Executor {
  public static final Executor INSTANCE = new Executor();
  
  
  private static String toString(String [] args) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i]);
      sb.append(" ");
    }
    return sb.toString();
  }
  
  private Executor() {}
  
  public String executeProcess(File inDir, String ... args) throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder(args);
    pb.redirectErrorStream(true);
    
    if (inDir != null) {
      pb.directory(inDir);
    }
    pb.redirectErrorStream(true);
    Process p = pb.start();
    p.waitFor();
    int exitValue = p.exitValue();
    
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    try {
      is = p.getInputStream();
      byte [] bytes = new byte[1];
      
      
      while (is.read(bytes) != -1) {
        baos.write(bytes);
      }
      
    } catch (IOException x) {
      throw x;
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
    if (exitValue != 0) {
      throw new IOException("Process exited abnormially " + exitValue + System.lineSeparator() +
          inDir.getAbsolutePath() + System.lineSeparator() +
          toString(args) + System.lineSeparator() +
          new String(baos.toByteArray()));
    }
    return new String(baos.toByteArray());
  }

}
