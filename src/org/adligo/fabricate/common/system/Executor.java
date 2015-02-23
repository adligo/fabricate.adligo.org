package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
public class Executor implements I_Executor {
  private final I_FabSystem sys_;
  private final I_FabFileIO files_;
  
  public static String toString(String [] args) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i]);
      sb.append(" ");
    }
    return sb.toString();
  }
  
  protected Executor(I_FabSystem system) {
    sys_ = system;
    files_ = system.getFileIO();
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_Executor#executeProcess(java.io.File, java.lang.String)
   */
  @Override
  public I_ExecutionResult executeProcess(String inDir, String ... args) throws IOException {
    ProcessBuilderWrapper pb = sys_.newProcessBuilder(args);
    pb.redirectErrorStream(true);
    
    File dir = null;
    if (inDir != null) {
      if (".".equals(inDir)) {
        File dirIn = files_.instance(inDir);
        inDir = dirIn.getAbsolutePath();
        char lastChar = inDir.charAt(inDir.length() -1);
        if ('.' == lastChar) {
          inDir = inDir.substring(0, inDir.length() - 2);
        }
      }
      dir = files_.instance(inDir);
      pb.directory(dir);
    }
    Process p = pb.start();
    try {
      p.waitFor();
    } catch (InterruptedException x) {
      sys_.currentThread().interrupt();
    }
    int exitValue = p.exitValue();
    
    InputStream is = p.getInputStream();
    
    StringBuilder sb = new StringBuilder();
    BufferedInputStream in = sys_.newBufferedInputStream(is);
    try {
      String line = in.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(sys_.lineSeperator());
        line = in.readLine();
      }
    } catch (IOException x) {
      throw x;
    } finally {
      in.close();
    }
    
    ExecutionResultMutant erm = new ExecutionResultMutant();
    erm.setExitCode(exitValue);
    String output = sb.toString();
    erm.setOutput(output);
    return erm;
  }

}
