package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.I_CommandAware;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_TaskProcessor;
import org.adligo.fabricate.routines.RoutineExecutionEngine;
import org.adligo.fabricate.routines.RoutineFactory;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandManager {
  private final List<String> commands_ = new ArrayList<String>();
  private final I_FabSystem system_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  private String command_;
  
  private final I_RoutineBuilder executorFactory_ = new I_RoutineBuilder() {
    

    @Override
    public I_FabricationRoutine build(I_FabricationMemoryMutant memory, I_RoutineMemoryMutant routineMemory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processCommandSetup();
      if (!toRet.setup(memory, routineMemory)) {
        return null;
      }
      return toRet;
    }

    @Override
    public I_FabricationRoutine build(I_FabricationMemory memory, I_RoutineMemory routineMemory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processCommandSetup();
      toRet.setup(memory, routineMemory);
      return toRet;
    }
  };
  
  public CommandManager(Collection<String> commands, I_FabSystem system, 
      RoutineFabricateFactory factory) {
    commands_.addAll(commands);
    system_ = system;
    factory_ = factory;
    fabricate_ = factory.getFabricate();
  }
  
  /**
   * @return FailureType if failure occurs, otherwise null.
   */
  public FailureType processCommands(FabricationMemoryMutant memory) {
    Throwable failure = null;
    I_FabricationRoutine routine = null;
    try {
      for (String command: commands_) {
        command_ = command;
        RoutineExecutionEngine exe = factory_.createRoutineExecutionEngine(system_, executorFactory_);
        exe.runRoutines(memory);
        if (exe.hadFailure()) {
          failure = exe.getFailure();
          routine = exe.getRoutineThatFailed();
          break;
        }
      }
    } catch (Throwable t) {
      failure = t;
    }
    if (failure != null) {
      FailureType result = new FailureType();
      result.setCommand(command_);
      if (routine != null) {
        if (I_TaskProcessor.class.isAssignableFrom(routine.getClass())) {
          result.setTask(((I_TaskProcessor) routine).getCurrentTask());
        }
      }
      ByteArrayOutputStream baos = system_.newByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      ps.append(system_.lineSeperator());
      failure.printStackTrace(ps);
      String detail = baos.toString();
      result.setDetail(detail);
      return result;
    }
    return null;
  }

  private I_FabricationRoutine processCommandSetup() throws FabricationRoutineCreationException{
    Set<I_ExpectedRoutineInterface> es = Collections.emptySet();
    I_FabricationRoutine routine = factory_.createCommand(command_,es);
    Map<String, I_RoutineBrief> cmds = fabricate_.getCommands();
    I_RoutineBrief brief = cmds.get(command_);
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
