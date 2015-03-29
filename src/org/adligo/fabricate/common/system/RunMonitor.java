package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.log.I_FabLog;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RunMonitor implements I_RunMonitor {
  private final I_FabSystem system_;
  private final I_LocatableRunnable delegate_;
  private final int sequence_;
  
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private ArrayBlockingQueue<Boolean> done_;
  private final AtomicReference<Throwable> caught_ = new AtomicReference<Throwable>();
  private Thread thread_;
  
  /**
   * use the I_FabSystem factory method for stubbing.
   * @param system
   * @param delegate
   * @param sequence
   */
  public RunMonitor(I_FabSystem system, I_LocatableRunnable delegate, int sequences) {
    system_ = system;
    done_ = system_.newArrayBlockingQueue(Boolean.class, 1);
    delegate_ = delegate;
    sequence_ = sequences;
  }
  
  @SuppressWarnings("boxing")
  @Override
  public void run() {
    thread_ = system_.currentThread();
    try {
      delegate_.run();
    } catch (Throwable t) {
      synchronized (this) {
        I_FabLog log = system_.getLog();
        if (!(t instanceof AlreadyLoggedException)) {
          log.printTrace(t);
        }
        //note fabricate doesn't try to recover from out of memory errors,
        // etc since the end result will be simply reporting major errors
        // to the user so that the user can fix them.
        caught_.set(t);
      }
    } 
    finished_.set(true);
    done_.add(true);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#isFinished()
   */
  @Override
  public synchronized boolean isFinished() {
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
  public synchronized boolean hasFailure() {
    if (caught_.get() == null) {
      return false;
    }
    return true;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#getCaught()
   */
  @Override
  public synchronized Throwable getCaught() {
    return caught_.get();
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#getDelegate()
   */
  @Override
  public synchronized I_LocatableRunnable getDelegate() {
    return delegate_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#getSequence()
   */
  @Override
  public synchronized int getSequence() {
    return sequence_;
  }

  public Thread getThread() {
    return thread_;
  }


}
