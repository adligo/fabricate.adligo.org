package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.I_FailureTransport;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineExecutor;
import org.adligo.fabricate.routines.I_RoutineFabricateProcessorFactory;
import org.adligo.fabricate.routines.I_RoutineProcessorFactory;

import java.util.List;

public class FabricationManager {

  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_SystemMessages sysMessages_;
  private final I_CommandLineConstants commandLineConstants_;
  private final I_RoutineProcessorFactory factory_;
  private final I_FabricateConstants constants_;
  private final I_Fabricate fab_;
  private final I_RoutineBuilder routineBuilder_;
  private final I_RoutineBuilder archiveBuilder_;
  
  public FabricationManager(I_FabSystem system,  I_RoutineFabricateProcessorFactory factory, 
      I_RoutineBuilder routineBuilder, I_RoutineBuilder archiveBuilder) {
    system_ = system;
    log_ = system.getLog();
    I_FabricateConstants constants = system.getConstants();
    sysMessages_ = constants.getSystemMessages();
    fab_ = factory.getFabricate();
    constants_ = system.getConstants();
    
    factory_ = factory;
    commandLineConstants_ = constants_.getCommandLineConstants();
    routineBuilder_ = routineBuilder;
    archiveBuilder_ = archiveBuilder;
  }
  
  public I_FailureTransport setupAndRunBuildStages(FabricationMemoryMutant<Object> memory) {
    List<String> stages = fab_.getStageOrder();
    
    String stagesKey = commandLineConstants_.getStages();
    List<String> clStages = system_.getArgValues(stagesKey);
    
    String skipKey = commandLineConstants_.getSkip();
    List<String> clSkip = system_.getArgValues(skipKey);
    
    for (String stage: stages) {
      I_RoutineBrief routine = fab_.getStage(stage);
      boolean run = false;
      if (routine.isOptional()) {
        if (clStages.contains(stage)) {
          if (!clSkip.contains(stage)) {
            run = true;
          }
        }
      } else {
        if (!clSkip.contains(stage)) {
          run = true;
        }
      }
      if (run) {
        if (log_.isLogEnabled(CommandManager.class)) {
          String message = sysMessages_.getRunningBuildStageX();
          message = message.replace("<X/>", stage);
          log_.println(message);
        }
        I_RoutineExecutor executor = factory_.createRoutineExecutor();
        routineBuilder_.setNextRoutineName(stage);
        I_FailureTransport result = executor.run(stage, routineBuilder_, memory);
        if (result != null) {
          return result;
        }
        if (log_.isLogEnabled(CommandManager.class)) {
          String message = sysMessages_.getBuildStageXCompletedSuccessfully();
          message = message.replace("<X/>", stage);
          log_.println(message);
        }
      }
    }
    
    String archive = commandLineConstants_.getArchive(true);
    if (system_.hasArg(archive)) {
      List<String> archiveStages = fab_.getArchiveStageOrder();
      for (String stage: archiveStages) {
        I_RoutineBrief routine = fab_.getArchiveStage(stage);
        boolean run = false;
        if (routine.isOptional()) {
          if (clStages.contains(stage)) {
            run = true;
          }
        } else {
          run = true;
        }
        if (run) {
          if (log_.isLogEnabled(CommandManager.class)) {
            String message = sysMessages_.getRunningArchiveStageX();
            message = message.replace("<X/>", stage);
            log_.println(message);
          }
          I_RoutineExecutor executor = factory_.createRoutineExecutor();
          archiveBuilder_.setNextRoutineName(stage);
          I_FailureTransport result = executor.run(stage, archiveBuilder_, memory);
          if (result != null) {
            return result;
          }
          if (log_.isLogEnabled(CommandManager.class)) {
            String message = sysMessages_.getArchiveStageXCompletedSuccessfully();
            message = message.replace("<X/>", stage);
            log_.println(message);
          }
        }
      }
    }
    return null;
  }
  
}
