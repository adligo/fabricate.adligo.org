package org.adligo.fabricate.common.system;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunMonitor implements I_RunMonitor {
  private final I_FabSystem system_;
  private final I_LocatableRunable delegate_;
  private final int sequence_;
  
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private ArrayBlockingQueue<Boolean> done_;
  private Throwable caught_;
  
  /**
   * use the I_FabSystem factory method for stubbing.
   * @param system
   * @param delegate
   * @param sequence
   */
  public RunMonitor(I_FabSystem system, I_LocatableRunable delegate, int sequence) {
    system_ = system;
    done_ = system_.newArrayBlockingQueue(Boolean.class, 1);
    delegate_ = delegate;
    sequence_ = sequence;
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
  public I_LocatableRunable getDelegate() {
    return delegate_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_RunMonitor#getSequence()
   */
  @Override
  public int getSequence() {
    return sequence_;
  }


}
