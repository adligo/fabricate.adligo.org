package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * If there is a better standard way to run Processes 
 * (in particular shell scripts) from java, 
 * please let me know as the doc says;<br/>
 * The methods that create processes may not work well for special processes 
 * on certain native platforms, such as native windowing processes, 
 * daemon processes, Win16/DOS processes on Microsoft Windows, or shell scripts.<br/>
 * <br/>
 *  <br/>
 *  Also please amend the Process documentation to say what should be
 * used in those cases.<br/>
 * http://docs.oracle.com/javase/8/docs/api/java/lang/Process.html<br/>
 * I found Process to run ok, but the IO seemed a bit sketchy,
 * as it didn't always seem to read all of stdin, stout;<br>
 * @author scott
 *
 */
public class ProcessRunnable implements I_ProcessRunnable {
  private final Process process_;
  private int exitValue_ = -1;
  private OutputStream processInput_;
  private InputStream processOutput_;
  /**
   * output from the process
   */
  private final ConcurrentLinkedQueue<String> processLinesOut_;
  /**
   * data to send to the process
   */
  private final ConcurrentLinkedQueue<ProcessOutputData> processDataIn_;
  private final I_Method waitForMethod_;
  
  /**
   * Assumes the process has already been started
   * with ProcessBuilder#start();
   * @param process note this assumes standard error and out are combined.
   */
  public ProcessRunnable(I_FabSystem sys, Process process, I_Method waitForMethod) {
    processLinesOut_ = sys.newConcurrentLinkedQueue(String.class);
    processDataIn_ = sys.newConcurrentLinkedQueue(ProcessOutputData.class);
    
    process_ = process;
    
    waitForMethod_ = waitForMethod;
    processInput_ = process_.getOutputStream();
    processOutput_ = process_.getInputStream();
  }

  @SuppressWarnings("boxing")
  @Override
  public void run() {
    try {
      IllegalThreadStateException stillExecutingException = new IllegalThreadStateException();
      byte [] bs = new byte[1];
      
      while (stillExecutingException != null) {
       
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (processOutput_.available() >= 1) {
          processOutput_.read(bs);
          baos.write(bs[0]);
        }
        String content = baos.toString("UTF-8");
        char [] chars = content.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean lastCharLineEnd = false;
        for (int i = 0; i < chars.length; i++) {
          char c = chars[i];
          if (c == '\n' || c == '\r') {
            if (lastCharLineEnd) {
              lastCharLineEnd = false;
            } else {
              String line = sb.toString();
              processLinesOut_.add(line);
              sb = new StringBuilder();
              lastCharLineEnd = true;
            }
          } else {
            sb.append(c);
            lastCharLineEnd = false;
          }
        }
        String line = sb.toString();
        if (line.length() >= 1) {
          processLinesOut_.add(line);
        }
        ProcessOutputData data = processDataIn_.poll();
        while (data != null) {
          byte [] dataBytes = null;
          String charSet = data.getCharSet();
          if (charSet == null) {
            dataBytes = new String(data.getData()).getBytes();
          } else {
            dataBytes = new String(data.getData()).getBytes(charSet);
          }
          processInput_.write(dataBytes);
          processInput_.flush();
          
          data = processDataIn_.poll();
        }
        
        if (waitForMethod_ != null) {
          //this is a small optimization on JDK 1.8 and higher
          try {
            waitForMethod_.invoke(process_, 1000L, TimeUnit.MILLISECONDS);
          } catch (IllegalAccessException | IllegalArgumentException |InvocationTargetException e) {
            //do nothing
          }
        }
        try {
          exitValue_ = process_.exitValue();
          stillExecutingException = null;
        } catch (IllegalThreadStateException x) {
          stillExecutingException = x;
        }
        
      }
    } catch (IOException x) {
      throw new RuntimeException(x);
    } finally {
      try {
        processInput_.close();
      } catch (IOException e) {
        //do nothing
      }
      try {
        processOutput_.close();
      } catch (IOException e) {
        //do nothing
      }
    }
  }
  
  /**
   * Not synchronized relying on ConcurrentLinkedQueue 
   * to handle passing data between threads.
   * 
   */
  public List<String> getOutput() {
    List<String> toRet = new ArrayList<String>();
    String line = processLinesOut_.poll();
    while (line != null) {
      toRet.add(line);
      line = processLinesOut_.poll();
    }
    return toRet;
  }

  /**
   * Not synchronized relying on ConcurrentLinkedQueue 
   * to handle passing data between threads.
   * 
   */
  public void writeInputToProcess(String input, String charSet) {
    if (StringUtils.isEmpty(input)) {
      throw new IllegalArgumentException();
    }
    processDataIn_.add(new ProcessOutputData(input, charSet));
  }

  public int getExitCode() {
    return exitValue_;
  }

  public void destroy() {
    process_.destroy();
  }
}
