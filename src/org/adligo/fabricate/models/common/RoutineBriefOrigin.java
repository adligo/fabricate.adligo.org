package org.adligo.fabricate.models.common;

public enum RoutineBriefOrigin {
  /**
   * A archive stage from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  ARCHIVE_STAGE, 
  /**
   * A archive stage/task from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  ARCHIVE_STAGE_TASK, 
  /**
   * A command from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  COMMAND, 
  /**
   * A command/task from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  COMMAND_TASK, 
  /**
   * A archive stage from fabricate.xml
   */
  FABRICATE_ARCHIVE_STAGE,
  /**
   * A archive stage/task from fabricate.xml
   */
  FABRICATE_ARCHIVE_STAGE_TASK,
  /**
   * A command from fabricate.xml
   */
  FABRICATE_COMMAND,
  /**
   * A command/task from fabricate.xml
   */
  FABRICATE_COMMAND_TASK,
  /**
   * A build stage from fabricate.xml
   */
  FABRICATE_STAGE,
  /**
   * A build stage/task from fabricate.xml
   */
  FABRICATE_STAGE_TASK,
  /**
   * A build stage from fabricate.xml
   */
  FABRICATE_TRAIT,
  /**
   * A build stage/task from fabricate.xml
   */
  FABRICATE_TRAIT_TASK,
  /**
   * From a Fabricate in memory Default.
   */
  IMPLICIT_COMMAND,
  /**
   * From a Fabricate in memory Default/task.
   */
  IMPLICIT_COMMAND_TASK,
  /**
   * From a Fabricate in memory Default.
   */
  IMPLICIT_STAGE,
  /**
   * From a Fabricate in memory Default/task.
   */
  IMPLICIT_STAGE_TASK,
  /**
   * From a Fabricate in memory Default.
   */
  IMPLICIT_TRAIT,
  /**
   * From a Fabricate in memory Default/task.
   */
  IMPLICIT_TRAIT_TASK,
  /**
   * From a project.xml command.
   */
  PROJECT_COMMAND, 
  /**
   * From a project.xml command/task.
   */
  PROJECT_COMMAND_TASK, 
  /**
   * From a project.xml stage.
   */
  PROJECT_STAGE, 
  /**
   * From a project.xml stage/task.
   */
  PROJECT_STAGE_TASK, 
  /**
   * A stage from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  STAGE, 
  /**
   * A stage/task from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  STAGE_TASK, 
  /**
   * A trait from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  TRAIT, 
  /**
   * A trait/task from a merged location including any of (implicit, fabricate.xml and project.xml).
   */
  TRAIT_TASK ;
  
  private RoutineBriefOrigin() {}
}
