package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.log.I_FabLog;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExecutingProcess implements I_ExecutingProcess {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final ProcessRunnable delegate_;
  
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private ArrayBlockingQueue<Boolean> done_;
  private Throwable caught_;
  
  /**
   * use the I_FabSystem factory method for stubbing.
   * @param system
   * @param delegate
   * @param sequence
   */
  public ExecutingProcess(I_FabSystem system, Process process) {
    system_ = system;
    log_ = system.getLog();
    done_ = system_.newArrayBlockingQueue(Boolean.class, 1);
    delegate_ = new ProcessRunnable(process);
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
    if (caught_ == null) {
      return false;
    }
    if (delegate_.getExitCode() == 0) {
      return false;
    }
    return true;
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
    return delegate_.getOutput();
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

}
