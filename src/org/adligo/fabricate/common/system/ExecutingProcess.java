package org.adligo.fabricate.common.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecutingProcess implements I_ExecutingProcess {
  private final I_FabSystem system_;
  private final I_ProcessRunnable delegate_;
  
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private ArrayBlockingQueue<Boolean> done_;
  private Throwable caught_;
  private List<String> allOutput_ = new ArrayList<String>();
  
  /**
   * use the I_FabSystem factory method for stubbing.
   * @param system
   * @param delegate
   * @param sequence
   */
  public ExecutingProcess(I_FabSystem system, Process process) {
    system_ = system;
    done_ = system_.newArrayBlockingQueue(Boolean.class, 1);
    delegate_ = system_.newProcessRunnable(process);
  }
  
  @SuppressWarnings("boxing")
  @Override
  public void run() {
    try {
      delegate_.run();
    } catch (Throwable t) {
      //note fabricate doesn't try to recover from out of memory errors,
      // etc since the end result will be simply reporting major errors
      // to the user so that the user can fix them.
      caught_ = t;
    }
    finished_.set(true);
    done_.add(true);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#isFinished()
   */
  @Override
  public boolean isFinished() {
    return finished_.get();
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#waitUntilFinished(long)
   */
  @Override
  public void waitUntilFinished(long millis) throws InterruptedException {
    done_.poll(millis, TimeUnit.MILLISECONDS);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#hasFailure()
   */
  public boolean hasFailure() {
    if (caught_ != null) {
      return true;
    }
    if (delegate_.getExitCode() != 0) {
      return true;
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#getCaught()
   */
  @Override
  public Throwable getCaught() {
    return caught_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#getDelegate()
   */
  @Override
  public Runnable getDelegate() {
    return delegate_;
  }

  @Override
  public List<String> getOutput() {
    List<String> ret = delegate_.getOutput();
    allOutput_.addAll(ret);
    return ret;
  }

  @Override
  public void writeInputToProcess(String input, String charSet) {
    delegate_.writeInputToProcess(input, charSet);
  }

  @Override
  public int getExitCode() {
    return delegate_.getExitCode();
  }

  public void destroy() {
    delegate_.destroy();
  }

  @Override
  public List<String> getAllOutput() {
    List<String> out = getOutput();
    allOutput_.addAll(out);
    return new ArrayList<String>(allOutput_);
  }

  public void throwIOExceptionWithAllOutput(String message) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(message);
    
    List<String> allOut = getAllOutput();
    for (String line: allOut) {
      sb.append(system_.lineSeparator());
      sb.append(line);
    }
    sb.append(system_.lineSeparator());
    throw new IOException(sb.toString());
  }
}
