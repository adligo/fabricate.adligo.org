package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.implicit.ImplicitStages;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabricationManager {
  private static final Map<String, I_RoutineBrief> IMPLICIT_ROUTINES = getImplicitRoutines();
  private static final List<String> IMPLICIT_STAGE_ORDER = getImplicitStageOrder();
  
  private static final Map<String, I_RoutineBrief> getImplicitRoutines() {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    toRet.put(ImplicitStages.JAR, ImplicitStages.JAR_BRIEF);
    return Collections.unmodifiableMap(toRet);
  }
  
  private static final List<String> getImplicitStageOrder() {
    List<String> toRet = new ArrayList<String>();
    toRet.add(ImplicitStages.JAR);
    return Collections.unmodifiableList(toRet);
  }
  
  private final I_CommandLineConstants commandLineConstants_;
  private final I_FabricateConstants constants_;
  private final StageExecutor executor_;
  private final I_Fabricate fab_;
  private final I_SystemMessages sysMessages_;
  private final StageSetup setup_;
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final String stages_;
  
  public FabricationManager(I_FabSystem system,  RoutineFabricateFactory factory, I_RepositoryManager rm) {
    executor_ = new StageExecutor(system, factory);
    fab_ = factory.getFabricate();
    log_ = system.getLog();
    setup_ = new StageSetup(system, factory, rm);
    system_ = system;
    constants_ = system.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    
    commandLineConstants_ = constants_.getCommandLineConstants();
    stages_ = commandLineConstants_.getStages();
  }
  
  @SuppressWarnings("unchecked")
  public FailureType setupAndRunBuildStages(FabricationMemoryMutant<Object> memory) {
    List<I_Project> projects = (List<I_Project>) memory.get(FabricationMemoryConstants.PARTICIPATING_PROJECTS);
    setup_.setProjects(projects);
    
    List<String> stages = fab_.getStageOrder();
    Map<String,I_RoutineBrief> routines = fab_.getStages();
    if (stages.isEmpty()) {
      stages = IMPLICIT_STAGE_ORDER;
      routines = IMPLICIT_ROUTINES;
    }
    for (String stage: stages) {
      I_RoutineBrief stageBrief = routines.get(stage);
      String name = stageBrief.getName();
      
      boolean execute = false;
      if (stageBrief.isOptional()) {
        List<String> stageList = system_.getArgValues(stages_);
        if (stageList.contains(name)) {
          execute = true;
        }
      } else {
        execute = true;
      }
      if (execute) {
        if (log_.isLogEnabled(CommandManager.class)) {
          String message = sysMessages_.getRunningBuildStageX();
          message = message.replace("<X/>", name);
          log_.println(message);
        }
        FailureType failure = executor_.run(name, setup_, memory);
        if (failure != null) {
          return failure;
        }
      }
    }
    return null;
  }
  
}
