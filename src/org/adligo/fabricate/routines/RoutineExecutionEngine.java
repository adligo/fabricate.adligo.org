package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.I_LocatableRunnable;
import org.adligo.fabricate.common.system.I_RunMonitor;
import org.adligo.fabricate.models.common.FabricationMemory;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RoutineExecutionEngine {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_RoutineBuilder factory_;
  private final int threads_;
  private final List<I_RunMonitor> monitors_ = new ArrayList<I_RunMonitor>();
  
  public RoutineExecutionEngine(I_FabSystem system, I_RoutineBuilder factory, int threads) {
    system_ = system;
    log_ = system.getLog();
    factory_ = factory;
    threads_ = threads;
  }
  
  public void runRoutines(FabricationMemoryMutant memoryMut)
      throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
      log_.println(RoutineExecutionEngine.class.getName() + " runRoutines(FabricationMemoryMutant)");
    }
    
    FabricationMemoryMutant routineMemoryMutant = new FabricationMemoryMutant();
    I_FabricationRoutine routine = factory_.build(memoryMut, routineMemoryMutant);
    
    if (routine == null) {
      if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
        log_.println("build of " + factory_.getClass().getName() + " returned null");
      }
      return;
    }
    if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
      log_.println("created " + routine.getClass());
    }
    FabricationMemory memory = new FabricationMemory(memoryMut);
    FabricationMemory routineMemory = new FabricationMemory(routineMemoryMutant);
    if (I_ConcurrencyAware.class.isAssignableFrom(routine.getClass())) {
      
      if (threads_ >= 2) {
        ExecutorService service =  system_.newFixedThreadPool(threads_);
        for (int i = 0; i < threads_; i++) {
          I_RunMonitor monitor = system_.newRunMonitor(routine, i + 1);
          if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
            log_.println("submiting " + routine.getClass());
          }
          service.submit(monitor);
          monitors_.add(monitor);
          routine = factory_.build(memory, routineMemory);
        }
        int counter = 0;
        while (true) {
          int monitorsFinished = 0;
          for (I_RunMonitor rm: monitors_) {
            if (rm.getSequence() == 1) {
              counter++;
            }
            if (!rm.isFinished()) {
              if (counter >= 3) {
                I_LocatableRunnable lr = rm.getDelegate();
                String message = lr.getCurrentLocation();
                String additional = lr.getAdditionalDetail();
                if (additional != null) {
                  message = message + system_.lineSeperator() +
                      additional + system_.lineSeperator() + system_.lineSeperator();
                }
                log_.println(message);
              }
              try {
                rm.waitUntilFinished(1000);
              } catch (InterruptedException e) {
                system_.currentThread().interrupt();
              }
            } else {
              monitorsFinished++;
            }
          }
          if (monitorsFinished == monitors_.size()) {
            return;
          } 
        }
      } else {
        I_RunMonitor monitor = system_.newRunMonitor(routine, 1);
        monitors_.add(monitor);
        monitor.run();
      }
    } else {
      I_RunMonitor monitor = system_.newRunMonitor(routine, 1);
      monitors_.add(monitor);
      monitor.run();
    }
  }
  
  public boolean hadFailure() {
    for (I_RunMonitor rm: monitors_) {
      if (rm.hasFailure()) {
        return true;
      }
    }
    return false;
  }
  
  public Throwable getFailure() {
    for (I_RunMonitor rm: monitors_) {
      if (rm.hasFailure()) {
        return rm.getCaught();
      }
    }
    return null;
  }
  
  public I_FabricationRoutine getRoutineThatFailed() {
    for (I_RunMonitor rm: monitors_) {
      if (rm.hasFailure()) {
        return (I_FabricationRoutine) rm.getDelegate();
      }
    }
    return null;
  }
}
