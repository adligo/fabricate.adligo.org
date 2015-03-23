package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.I_ExecutionEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

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
  public I_ExecutionResult executeProcess(I_ExecutionEnvironment env, String inDir, String ... args) throws IOException {
    I_ProcessBuilderWrapper pb = sys_.newProcessBuilder(args);
    pb.redirectErrorStream(true);
    Map<String,String> environment = pb.environment();
    env.addAllTo(environment);
    
    setProcessDir(inDir, pb); 
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
        sb.append(sys_.lineSeparator());
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

  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_Executor#startProcess(I_ExecutionEnvironment env, ExecutorService service, String inDir, String ... args)
   */
  @Override
  public I_ExecutingProcess startProcess(I_ExecutionEnvironment env, ExecutorService service, String inDir, String ... args) throws IOException {
    return startProcessPrivate(env, service, inDir, args);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_Executor#startProcess(I_ExecutionEnvironment env, ExecutorService service, String inDir, List<String> args)
   */
  @Override
  public I_ExecutingProcess startProcess(I_ExecutionEnvironment env, ExecutorService service, String inDir, List<String> args) throws IOException {
    return startProcessPrivate(env, service, inDir, args.toArray(new String[args.size()]));
  }
  
  private I_ExecutingProcess startProcessPrivate(I_ExecutionEnvironment env, ExecutorService service,
      String inDir, String [] args) throws IOException {
    I_ProcessBuilderWrapper pb = sys_.newProcessBuilder(args);
    pb.redirectErrorStream(true);
    Map<String,String> environment = pb.environment();
    env.addAllTo(environment);
    
    setProcessDir(inDir, pb); 
    Process p = pb.start();
    I_ExecutingProcess ep = sys_.newExecutingProcess(p);
    service.execute(ep);
    return ep;
  }

  private void setProcessDir(String inDir, I_ProcessBuilderWrapper pb) {
    File dir = null;
    if (StringUtils.isEmpty(inDir)) {
      dir = files_.instance(".");
      pb.directory(dir);
    } else {
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
  }
}
