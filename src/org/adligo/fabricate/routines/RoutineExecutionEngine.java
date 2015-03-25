package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoutineExecutionEngine {
  private final I_FabSystem system_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  private final I_RoutineBuilder factory_;
  private final int threads_;
  private final List<I_RunMonitor> monitors_ = new ArrayList<I_RunMonitor>();
  private final AtomicBoolean failure_  = new AtomicBoolean(false);
  private Throwable failureThrowable_;
  private I_FabricationRoutine routineFailure_;
  
  
  public RoutineExecutionEngine(I_FabSystem system, I_RoutineBuilder factory, int threads) {
    system_ = system;
    I_FabricateConstants constants = system.getConstants();
    sysMessages_ = constants.getSystemMessages();
    
    log_ = system.getLog();
    factory_ = factory;
    threads_ = threads;
  }
  
  @SuppressWarnings("static-access")
  public void runRoutines(FabricationMemoryMutant<Object> memoryMut)
      throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
      log_.println(RoutineExecutionEngine.class.getName() + " runRoutines(FabricationMemoryMutant)");
    }
    
    FabricationMemoryMutant<Object> routineMemoryMutant = new FabricationMemoryMutant<Object>(sysMessages_);
    I_FabricationRoutine firstRoutine = factory_.buildInitial(memoryMut, routineMemoryMutant);
    
    if (firstRoutine == null) {
      if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
        log_.println("build of " + factory_.getClass().getName() + " returned null");
      }
      return;
    }
    if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
      log_.println("created " + firstRoutine.getClass());
    }
    FabricationMemory<Object> memory = new FabricationMemory<Object>(memoryMut);
    FabricationMemory<Object> routineMemory = new FabricationMemory<Object>(routineMemoryMutant);
    if (I_ConcurrencyAware.class.isAssignableFrom(firstRoutine.getClass())) {
      
      if (threads_ >= 2) {
        ExecutorService service =  system_.newFixedThreadPool(threads_);
        I_FabricationRoutine routine = firstRoutine;
        for (int i = 0; i < threads_; i++) {
          I_RunMonitor monitor = system_.newRunMonitor(routine, i + 1);
          if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
            log_.println("submiting " + firstRoutine.getClass());
          }
          service.submit(monitor);
          monitors_.add(monitor);
          routine = factory_.build(memory, routineMemory);
        }
        int counter = 0;
        AtomicBoolean running = new AtomicBoolean(true);
        while (running.get()) {
          int monitorsFinished = 0;
          for (I_RunMonitor rm: monitors_) {
           if (!rm.isFinished()) {
              if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
                I_LocatableRunnable lr = rm.getDelegate();
                String message = lr.getCurrentLocation();
                String additional = lr.getAdditionalDetail();
                if (additional != null) {
                  message = message + system_.lineSeparator() +
                      additional + system_.lineSeparator() + system_.lineSeparator();
                }
                log_.println(message);
              }
              try {
                rm.waitUntilFinished(1000);
              } catch (InterruptedException e) {
                system_.currentThread().interrupt();
              }
              if (rm.hasFailure()) {
                failure_.set(true);
                failureThrowable_ = rm.getCaught();
                routineFailure_ = (I_FabricationRoutine) rm.getDelegate();
                break;
              }
            } else {
              monitorsFinished++;
            }
          }
          if (!failure_.get()) {
            for (I_RunMonitor rm: monitors_) {
              if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
                log_.println(RoutineExecutionEngine.class.getSimpleName() + " checking " + rm.getDelegate() +
                    " which hasFailure() ? " + rm.hasFailure());
              }
              if (rm.hasFailure()) {
                if (failureThrowable_ == null) {
                  failureThrowable_ = rm.getCaught();
                  if (log_.isLogEnabled(RoutineExecutionEngine.class)) {
                    log_.println(RoutineExecutionEngine.class.getSimpleName() + " failureThrowable " + failureThrowable_);
                  }
                  routineFailure_ = (I_FabricationRoutine) rm.getDelegate();
                }
                failure_.set(true);
                break;
              }
            }
          }
          if (failure_.get()) {
            processFailure(service, running);
            break;
          }
          if (monitorsFinished == monitors_.size()) {
            
            firstRoutine.writeToMemory(memoryMut);
            service.shutdownNow();
            running.set(false);
            return;
          } 
          try {
            system_.currentThread().sleep(1000);
          } catch (InterruptedException e) {
            log_.printTrace(e);
            system_.currentThread().interrupt();
          }
        }
      } else {
        I_RunMonitor monitor = system_.newRunMonitor(firstRoutine, 1);
        monitors_.add(monitor);
        monitor.run();
        if (monitor.hasFailure()) {
          failure_.set(true);
          failureThrowable_ = monitor.getCaught();
          routineFailure_ = firstRoutine;
        }
      }
    } else {
      I_RunMonitor monitor = system_.newRunMonitor(firstRoutine, 1);
      monitors_.add(monitor);
      monitor.run();
      if (monitor.hasFailure()) {
        failure_.set(true);
        failureThrowable_ = monitor.getCaught();
        routineFailure_ = firstRoutine;
      }
    }
  }

  private void processFailure(ExecutorService service, AtomicBoolean running) {
    for (I_RunMonitor rm: monitors_) {
      rm.getThread().interrupt();
    }
    running.set(false);
    service.shutdownNow();
  }
  
  public boolean hadFailure() {
    return failure_.get();
  }
  
  public Throwable getFailure() {
    return failureThrowable_;
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
