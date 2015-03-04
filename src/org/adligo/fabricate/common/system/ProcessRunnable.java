package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessRunnable implements I_ProcessRunnable {
  private final Process process_;
  private int exitValue_ = -1;
  OutputStream processInput_;
  InputStream processOutput_;
  /**
   * output from the process
   */
  private ConcurrentLinkedQueue<String> processLinesOut = new ConcurrentLinkedQueue<String>();
  /**
   * data to send to the process
   */
  private ConcurrentLinkedQueue<ProcessOutputData> processDataIn = new ConcurrentLinkedQueue<ProcessOutputData>();
  
  /**
   * Assumes the process has already been started
   * with ProcessBuilder#start();
   * @param process note this assumes standard error and out are combined.
   */
  public ProcessRunnable(Process process) {
    process_ = process;
    processInput_ = process_.getOutputStream();
    processOutput_ = process_.getInputStream();
  }

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
              processLinesOut.add(line);
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
          processLinesOut.add(line);
        }
        ProcessOutputData data = processDataIn.poll();
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
          
          data = processDataIn.poll();
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
  
  public List<String> getOutput() {
    List<String> toRet = new ArrayList<String>();
    String line = processLinesOut.poll();
    while (line != null) {
      toRet.add(line);
      line = processLinesOut.poll();
    }
    return toRet;
  }

  public void writeInputToProcess(String input, String charSet) {
    if (StringUtils.isEmpty(input)) {
      throw new IllegalArgumentException();
    }
    processDataIn.add(new ProcessOutputData(input, charSet));
  }

  public int getExitCode() {
    return exitValue_;
  }

  public void destroy() {
    process_.destroy();
  }
}
