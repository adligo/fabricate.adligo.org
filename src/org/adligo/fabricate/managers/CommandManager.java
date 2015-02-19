package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;
import org.adligo.fabricate.routines.I_CommandAware;
import org.adligo.fabricate.routines.I_RoutineExecutorFactory;
import org.adligo.fabricate.routines.RoutineExecutor;
import org.adligo.fabricate.routines.RoutineFabricateFactory;
import org.adligo.fabricate.routines.RoutineFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandManager {
  private final List<String> commands_ = new ArrayList<String>();
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  private String command_;
  private final I_RoutineExecutorFactory executorFactory_ = new I_RoutineExecutorFactory() {
    

    @Override
    public I_FabricationRoutine create(I_FabricationMemoryMutant memory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processCommandSetup();
      if (!toRet.setup(memory)) {
        return null;
      }
      return toRet;
    }

    @Override
    public I_FabricationRoutine create(I_FabricationMemory memory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processCommandSetup();
      toRet.setup(memory);
      return toRet;
    }
  };
  
  public CommandManager(Collection<String> commands, I_FabSystem system, 
      RoutineFabricateFactory factory) {
    commands_.addAll(commands);
    system_ = system;
    log_ = system.getLog();
    factory_ = factory;
    fabricate_ = factory.getFabricate();
  }
  
  public void processCommands() throws FabricationRoutineCreationException {
    for (String command: commands_) {
      command_ = command;
      RoutineExecutor exe = new RoutineExecutor(system_, executorFactory_, fabricate_.getThreads());
      exe.run();
    }
  }

  private I_FabricationRoutine processCommandSetup() throws FabricationRoutineCreationException{
    I_FabricationRoutine routine = factory_.createCommand(command_, Collections.emptySet());
    I_RoutineBrief brief = fabricate_.getCommands().get(command_);
    routine.setSystem(system_);
    RoutineFactory cmdFactory = factory_.getCommands();
    I_RoutineFactory taskFactory = cmdFactory.createTaskFactory(command_);
    routine.setTaskFactory(taskFactory);
    routine.setBrief(brief);
    routine.setTraitFactory(factory_.getTraits());
    if (I_CommandAware.class.isAssignableFrom(routine.getClass())) {
      ((I_CommandAware) routine).setCommandFactory(cmdFactory);
    }
    return routine;
  }
}
