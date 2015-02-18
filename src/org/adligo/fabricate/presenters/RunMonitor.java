package org.adligo.fabricate.presenters;

import org.adligo.fabricate.common.system.I_FabSystem;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunMonitor implements Runnable {
  private final I_FabSystem system_;
  private final Runnable delegate_;
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private ArrayBlockingQueue<Boolean> done_ = new ArrayBlockingQueue<Boolean>(1);
  private Throwable caught_;
  
  public RunMonitor(I_FabSystem system, Runnable delegate) {
    system_ = system;
    delegate_ = delegate;
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
  
  @SuppressWarnings("boxing")
  public void waitUntilFinished() throws InterruptedException {
    done_.take();
  }
  
  public Throwable getCaught() {
    return caught_;
  }
  
}
