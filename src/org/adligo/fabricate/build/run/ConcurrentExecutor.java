package org.adligo.fabricate.build.run;

import org.adligo.fabricate.common.I_FabStage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentExecutor {
  private I_FabStage stage_;
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
      Future<?> future = service_.submit(stage_);
      futures_.add(future);
    }
  }

  public I_FabStage getTask() {
    return stage_;
  }

  public void setTask(I_FabStage task) {
    stage_ = task;
  }
  
  public void waitUntilFinished() {
    stage_.waitUntilFinished();
  }

  public int getThreads() {
    return threads_;
  }
}
