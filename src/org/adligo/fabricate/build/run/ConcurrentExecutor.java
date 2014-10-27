package org.adligo.fabricate.build.run;

import org.adligo.fabricate.common.I_FabTask;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentExecutor {
  private I_FabTask task_;
  private int threads_ = Runtime.getRuntime().availableProcessors() * 2;
  private ExecutorService service_;
  private List<Future<?>> futures_ = new CopyOnWriteArrayList<Future<?>>();
  
  public ConcurrentExecutor() {}
  
  public void setThreads(int threads) {
    threads_ = threads;
    
  }
  
  public void execute() {
    if (threads_ <= 1) {
      service_ = Executors.newSingleThreadExecutor();
    } else {
      service_ = Executors.newFixedThreadPool(threads_);
    }
    futures_.clear();
    for (int i = 0; i <= threads_; i++) {
      Future<?> future = service_.submit(task_);
      futures_.add(future);
    }
  }

  public I_FabTask getTask() {
    return task_;
  }

  public void setTask(I_FabTask task) {
    task_ = task;
  }
  
  public void waitUntilFinished() {
    while ( !task_.isFinished()) {
      try {
        Thread.sleep(250);
      } catch (InterruptedException x) {
        x.printStackTrace();
      }
    }
    service_.shutdownNow();
  }

  public int getThreads() {
    return threads_;
  }
}
