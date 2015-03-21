package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

import java.util.List;

public class FabricationManager {
  private final I_CommandLineConstants commandLineConstants_;
  private final I_FabricateConstants constants_;
  private final StageExecutor executor_;
  private final I_Fabricate fab_;
  private final StageSetup setup_;
  private final I_FabSystem system_;
  private final String stages_;
  
  public FabricationManager(I_FabSystem system,  RoutineFabricateFactory factory, I_RepositoryManager rm) {
    executor_ = new StageExecutor(system, factory);
    fab_ = factory.getFabricate();
    setup_ = new StageSetup(system, factory, rm);
    system_ = system;
    constants_ = system.getConstants();
    commandLineConstants_ = constants_.getCommandLineConstants();
    stages_ = commandLineConstants_.getStages();
  }
  
  @SuppressWarnings("unchecked")
  public FailureType setupAndRun(FabricationMemoryMutant<Object> memory) {
    List<I_Project> projects = (List<I_Project>) memory.get(FabricationMemoryConstants.LOADED_PROJECTS);
    setup_.setProjects(projects);
    
    List<String> stages = fab_.getStageOrder();
    for (String stage: stages) {
      I_RoutineBrief stageBrief = fab_.getStage(stage);
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
        FailureType failure = executor_.run(name, setup_, memory);
        if (failure != null) {
          return failure;
        }
      }
    }
    return null;
  }
}
