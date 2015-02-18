package org.adligo.fabricate.presenters;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;
import org.adligo.fabricate.routines.I_ConcurrencyAware;
import org.adligo.fabricate.routines.RoutineFabricateFactory;
import org.adligo.fabricate.routines.RoutineFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandPhasePresenter {
  private final List<String> commands_ = new ArrayList<String>();
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  
  
  public CommandPhasePresenter(Collection<String> commands, I_FabSystem system, 
      RoutineFabricateFactory factory) {
    commands_.addAll(commands);
    system_ = system;
    log_ = system.getLog();
    factory_ = factory;
    fabricate_ = factory.getFabricate();
  }
  
  public void processCommands() throws FabricationRoutineCreationException {
    for (String command: commands_) {
      I_FabricationRoutine routine = factory_.createCommand(command, Collections.emptySet());
      processCommandSetup(command,routine);
      if (I_ConcurrencyAware.class.isAssignableFrom(routine.getClass())) {
        int threads = fabricate_.getThreads();
        List<RunMonitor> monitors = new ArrayList<RunMonitor>();
        if (threads >= 2) {
          ExecutorService service =  Executors.newFixedThreadPool(threads);
          for (int i = 0; i < threads; i++) {
            RunMonitor monitor = new RunMonitor(system_, routine);
            service.submit(monitor);
            monitors.add(monitor);
            routine = factory_.createCommand(command, Collections.emptySet());
            processCommandSetup(command,routine);
          }
          while (true) {
            boolean doneLoop = true;
            for (RunMonitor rm: monitors) {
              if (!rm.isFinished()) {
                try {
                  rm.waitUntilFinished();
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
        log_.println("processing command " + command);
        routine.run();
      }
    }
  }
  
  private void processCommandSetup(String command, I_FabricationRoutine routine) throws FabricationRoutineCreationException{
    I_RoutineBrief brief = fabricate_.getCommands().get(command);
    routine.setSystem(system_);
    RoutineFactory cmdFactory = factory_.getCommands();
    I_RoutineFactory taskFactory = cmdFactory.createTaskFactory(command);
    routine.setTaskFactory(taskFactory);
    routine.setBrief(brief);
    routine.setTraitFactory(factory_.getTraits());
    routine.setup();
  }
}
