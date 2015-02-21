package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.I_RunMonitor;
import org.adligo.fabricate.models.common.FabricationMemory;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoutineExecutor {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_RoutineBuilder factory_;
  private final int threads_;
  
  public RoutineExecutor(I_FabSystem system, I_RoutineBuilder factory, int threads) {
    system_ = system;
    log_ = system.getLog();
    factory_ = factory;
    threads_ = threads;
  }
  
  public void runRoutines()
      throws FabricationRoutineCreationException {
    
    FabricationMemoryMutant memoryMut = new FabricationMemoryMutant();
    I_FabricationRoutine routine = factory_.build(memoryMut);
    if (routine == null) {
      return;
    }
    FabricationMemory memory = new FabricationMemory(memoryMut);
    
    if (I_ConcurrencyAware.class.isAssignableFrom(routine.getClass())) {
      List<I_RunMonitor> monitors = new ArrayList<I_RunMonitor>();
      if (threads_ >= 2) {
        ExecutorService service =  Executors.newFixedThreadPool(threads_);
        for (int i = 0; i < threads_; i++) {
          I_RunMonitor monitor = system_.newRunMonitor(routine, i + 1);
          service.submit(monitor);
          monitors.add(monitor);
          routine = factory_.build(memory);
        }
        while (true) {
          boolean doneLoop = true;
          for (I_RunMonitor rm: monitors) {
            if (!rm.isFinished()) {
              try {
                rm.waitUntilFinished(1000);
              } catch (InterruptedException e) {
                log_.printTrace(e);
              }
            }
          }
          if (doneLoop) {
            break;
          } 
        }
      } else {
        routine.run();
      }
    } else {
      routine.run();
    }
  }
}
