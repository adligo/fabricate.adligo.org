package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.AlreadyLoggedException;
import org.adligo.fabricate.common.system.FailureTransport;
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
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.routines.I_CommandAware;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_RepositoryFactoryAware;
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
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  private I_RepositoryFactory repositoryFactory_;
  private String command_;
  
  private final I_RoutineBuilder executorFactory_ = new I_RoutineBuilder() {
    

    @Override
    public I_FabricationRoutine buildInitial(I_FabricationMemoryMutant<Object> memory, 
        I_RoutineMemoryMutant<Object> routineMemory)
        throws FabricationRoutineCreationException {
      I_FabricationRoutine toRet = processCommandSetup();
      if (!toRet.setupInitial(memory, routineMemory)) {
        return null;
      }
      return toRet;
    }

    @Override
    public I_FabricationRoutine build(I_FabricationMemory<Object> memory, 
        I_RoutineMemory<Object> routineMemory)
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
    log_ = system.getLog();
    constants_ = system.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    factory_ = factory;
    fabricate_ = factory.getFabricate();
  }
  
  /**
   * @return FailureType if failure occurs, otherwise null.
   */
  public FailureTransport processCommands(FabricationMemoryMutant<Object> memory) {
    Throwable failure = null;
    I_FabricationRoutine routine = null;
    try {
      for (String command: commands_) {
        if (log_.isLogEnabled(CommandManager.class)) {
          String message = sysMessages_.getRunningCommandX();
          message = message.replace("<X/>", command);
          log_.println(message);
        }
        command_ = command;
        RoutineExecutionEngine exe = factory_.createRoutineExecutionEngine(system_, executorFactory_);
        exe.runRoutines(memory);
        if (exe.hadFailure()) {
          failure = exe.getFailure();
          routine = exe.getRoutineThatFailed();
          break;
        }
      }
    } catch (FabricationRoutineCreationException x) {
      failure = x;
      Class<?> routineClass = x.getRoutine();
      log_.println("There was a problem using the following class;" +
          routineClass);
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
      ps.append(system_.lineSeparator());
      failure.printStackTrace(ps);
      String detail = baos.toString();
      result.setDetail(detail);
      return new FailureTransport(failure instanceof AlreadyLoggedException, result);
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
    if (routine instanceof I_CommandAware) {
      ((I_CommandAware) routine).setCommandFactory(cmdFactory);
    }
    if (routine instanceof I_FabricateAware) {
      ((I_FabricateAware) routine).setFabricate(fabricate_);
    }
    if (routine instanceof I_RepositoryFactoryAware) {
      ((I_RepositoryFactoryAware) routine).setRepositoryFactory(repositoryFactory_);
    }
    return routine;
  }

  public I_RepositoryFactory getRepositoryFactory() {
    return repositoryFactory_;
  }

  public void setRepositoryFactory(I_RepositoryFactory repositoryFactory) {
    this.repositoryFactory_ = repositoryFactory;
  }
}
