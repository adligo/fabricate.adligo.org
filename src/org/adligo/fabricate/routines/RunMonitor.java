package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.system.I_FabSystem;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunMonitor implements Runnable {
  private final I_FabSystem system_;
  private final Runnable delegate_;
  private final int sequence_;
  
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private ArrayBlockingQueue<Boolean> done_ = new ArrayBlockingQueue<Boolean>(1);
  private Throwable caught_;
  
  public RunMonitor(I_FabSystem system, Runnable delegate, int sequence) {
    system_ = system;
    delegate_ = delegate;
    sequence_ = sequence;
  }
  @SuppressWarnings("boxing")
  @Override
  public void run() {
    try {
      delegate_.run();
    } catch (Throwable t) {
      caught_ = t;
    }
    try {
      finished_.set(true);
      done_.put(true);
    } catch (InterruptedException e) {
      system_.currentThread().interrupt();
    }
  }

  public boolean isFinished() {
    return finished_.get();
  }
  
  public void waitUntilFinished(long millis) throws InterruptedException {
    done_.poll(millis, TimeUnit.MILLISECONDS);
  }
  
  public Throwable getCaught() {
    return caught_;
  }
  
  public int getSequence() {
    return sequence_;
  }
}
