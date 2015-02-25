package org.adligo.fabricate.common.log;

import org.adligo.fabricate.common.files.I_FabFileIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is a thread safe non blocking logger, which 
 * writes to a file while Fabricate is executing.
 * 
 * @author scott
 *
 */
public class FabFileLog implements I_FabFileLog {
  private final ConcurrentLinkedQueue<String> lineQueue = new ConcurrentLinkedQueue<String>();
  private final ConcurrentLinkedQueue<Throwable> throwableQueue = new ConcurrentLinkedQueue<Throwable>();
  private final OutputStream out;
  private final PrintStream ps;
  
  public FabFileLog(String file, I_FabFileIO files) throws FileNotFoundException, IOException {
    out = files.newFileOutputStream(file);
    ps = new PrintStream(out);
  }
  
  public void close() {
    ps.close();
    try {
      out.close();
    } catch (IOException e) {
      //do nothing
    }
  }

  @Override
  public void println(String p) {
    lineQueue.add(p);
    String next = lineQueue.poll();
    while (next != null) {
      ps.println(next);
      next = lineQueue.poll();
    }
  }

  @Override
  public void printTrace(Throwable t) {
    throwableQueue.add(t);
    Throwable next = throwableQueue.poll();
    while (next != null) {
      next.printStackTrace(ps);
      Throwable caught = next.getCause();
      while (caught != null) {
        caught.printStackTrace(ps);
        caught = caught.getCause();
      }
      next = throwableQueue.poll();
    }
  }
  
}
