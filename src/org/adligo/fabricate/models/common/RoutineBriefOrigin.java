package org.adligo.fabricate.models.common;

public enum RoutineBriefOrigin {
  COMMAND, COMMAND_TASK, PROJECT_COMMAND, PROJECT_COMMAND_TASK, 
  PROJECT_STAGE, PROJECT_STAGE_TASK, STAGE, STAGE_TASK, TRAIT, TRAIT_TASK ;
  
  private RoutineBriefOrigin() {}
}
