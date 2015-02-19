package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemory;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoutineExecutor {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_RoutineExecutorFactory factory_;
  private final int threads_;
  
  public RoutineExecutor(I_FabSystem system, I_RoutineExecutorFactory factory, int threads) {
    system_ = system;
    log_ = system.getLog();
    factory_ = factory;
    threads_ = threads;
  }
  
  public void run()
      throws FabricationRoutineCreationException {
    
    FabricationMemoryMutant memoryMut = new FabricationMemoryMutant();
    I_FabricationRoutine routine = factory_.create(memoryMut);
    FabricationMemory memory = new FabricationMemory(memoryMut);
    
    if (I_ConcurrencyAware.class.isAssignableFrom(routine.getClass())) {
      List<RunMonitor> monitors = new ArrayList<RunMonitor>();
      if (threads_ >= 2) {
        ExecutorService service =  Executors.newFixedThreadPool(threads_);
        for (int i = 0; i < threads_; i++) {
          RunMonitor monitor = new RunMonitor(system_, routine, i + 1);
          service.submit(monitor);
          monitors.add(monitor);
          routine = factory_.create(memory);
        }
        while (true) {
          boolean doneLoop = true;
          for (RunMonitor rm: monitors) {
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
