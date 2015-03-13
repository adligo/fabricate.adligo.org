package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_ImplicitTraitMessages;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.fabricate.I_FabricateXmlDiscovery;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRoutine implements I_FabricationRoutine {
  protected I_RoutineBrief brief_;
  protected I_FabSystem system_;
  protected I_FabFileIO files_;
  protected I_FabXmlFileIO xmlFiles_;
  protected I_FabricateConstants constants_;
  protected I_CommandLineConstants cmdConstants_;
  protected I_SystemMessages sysMessages_;
  
  protected I_ImplicitTraitMessages implicit_;
  protected I_FabLog log_;
  protected I_RoutineFactory traitFactory_;
  protected I_RoutineFactory taskFactory_;
  protected I_FabricateXmlDiscovery locations_;
  private AtomicBoolean settingUp = new AtomicBoolean(false);
  private AtomicBoolean running = new AtomicBoolean(false);
  
  /**
   * Overrides of this method should call this method.
   */
  @Override
  public void run() {
    running.set(true);
  }
  
  public String getAdditionalDetail() {
    return null;
  }
  
  public I_RoutineBrief getBrief() {
    return brief_;
  }
  
  
  @Override
  public I_RoutineFactory getTaskFactory() {
    return taskFactory_;
  }
  @Override
  public I_RoutineFactory getTraitFactory() {
    return traitFactory_;
  }
  
  @Override
  public I_FabSystem getSystem() {
    return system_;
  }
  @Override
  public void setTaskFactory(I_RoutineFactory factory) {
    taskFactory_ = factory;
  }
  
  @Override
  public void setTraitFactory(I_RoutineFactory factory) {
    traitFactory_ = factory;
  }

  @Override
  public void setSystem(I_FabSystem system) {
    system_ = system;
    files_ = system.getFileIO();
    xmlFiles_ = system.getXmlFileIO();
    
    log_ = system.getLog();
    constants_ = system.getConstants();
    implicit_ = constants_.getImplicitTraitMessages();
    sysMessages_ = constants_.getSystemMessages();
    cmdConstants_ = constants_.getCommandLineConstants();
  }

  public void setBrief(I_RoutineBrief brief) {
    this.brief_ = brief;
  }

  @Override
  public I_FabricateXmlDiscovery getLocations() {
    return locations_;
  }

  @Override
  public void setLocations(I_FabricateXmlDiscovery locations) {
    locations_ = locations;
  }

  /**
   * Do nothing, allow extension classes to override.
   */
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(AbstractRoutine.class)) {
      log_.println(AbstractRoutine.class.getName() + " setup(I_FabricationMemoryMutant, I_RoutineMemoryMutant)");
    }
    settingUp.set(true);
    return true;
  }

  /**
   * /**
   * Overrides of this method should call this method.
   */
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory) throws FabricationRoutineCreationException {
    settingUp.set(true);
  }

  @Override
  public String getCurrentLocation() {
    RoutineBriefOrigin rbo = brief_.getOrigin();
    switch (rbo) {
      case COMMAND:
      case FABRICATE_COMMAND:
      case IMPLICIT_COMMAND:
      case PROJECT_COMMAND:
        return getCommandCurrentLocation();
      case FACET:  
      case IMPLICIT_FACET:
      case FABRICATE_FACET:
        return getFacetCurrentLocation();
      case STAGE:
      case FABRICATE_STAGE:
      case IMPLICIT_STAGE:
      case PROJECT_STAGE:
        return getBuildCurrentLocation();
      case TRAIT:  
      case IMPLICIT_TRAIT:
      case FABRICATE_TRAIT:
        return getTraitCurrentLocation();
      case ARCHIVE_STAGE:
      case FABRICATE_ARCHIVE_STAGE:
        return getArchiveCurrentLocation();
      default:
    }
    return AbstractRoutine.class.getName() + ".getCurrentLocation() unknown location for " + rbo;
  }

  private String getArchiveCurrentLocation() {
    if (!running.get()) {
      if (settingUp.get()) {
        String message = sysMessages_.getArchiveStageXIsStillSettingUp();
        message = message.replace("<X/>", brief_.getName());
        return message;
      }
    }
    String task = null;
    if (I_TaskProcessor.class.isAssignableFrom(this.getClass())) {
      task = ((I_TaskProcessor) this).getCurrentTask();
    }
    String project = null;
    if (I_ProjectProcessor.class.isAssignableFrom(this.getClass())) {
      project = ((I_ProjectProcessor) this).getCurrentProject();
    }
    if (task != null && project != null) {
      String message = sysMessages_.getArchiveStageXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task)
          .replace("<Z/>", project);
      return message;
    } else if (task != null) {
      String message = sysMessages_.getArchiveStageXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task);
      return message;
    } else if (project != null) {
      String message = sysMessages_.getArchiveStageXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", project);
      return message;
    } else {
      String message = sysMessages_.getArchiveStageXIsStillRunning();
      message = message.replace("<X/>", brief_.getName());
      return message;
    }
  }

  private String getBuildCurrentLocation() {
    if (!running.get()) {
      if (settingUp.get()) {
        String message = sysMessages_.getBuildStageXIsStillSettingUp();
        message = message.replace("<X/>", brief_.getName());
        return message;
      }
    }
    String task = null;
    if (I_TaskProcessor.class.isAssignableFrom(this.getClass())) {
      task = ((I_TaskProcessor) this).getCurrentTask();
    }
    String project = null;
    if (I_ProjectProcessor.class.isAssignableFrom(this.getClass())) {
      project = ((I_ProjectProcessor) this).getCurrentProject();
    }
    if (task != null && project != null) {
      String message = sysMessages_.getBuildStageXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task)
          .replace("<Z/>", project);
      return message;
    } else if (task != null) {
      String message = sysMessages_.getBuildStageXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task);
      return message;
    } else if (project != null) {
      String message = sysMessages_.getBuildStageXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", project);
      return message;
    } else {
      String message = sysMessages_.getBuildStageXIsStillRunning();
      message = message.replace("<X/>", brief_.getName());
      return message;
    }
  }
  
  private String getCommandCurrentLocation() {
    if (!running.get()) {
      if (settingUp.get()) {
        String message = sysMessages_.getCommandXIsStillSettingUp();
        message = message.replace("<X/>", brief_.getName());
        return message;
      }
    }
    String task = null;
    if (I_TaskProcessor.class.isAssignableFrom(this.getClass())) {
      task = ((I_TaskProcessor) this).getCurrentTask();
    }
    String project = null;
    if (I_ProjectProcessor.class.isAssignableFrom(this.getClass())) {
      project = ((I_ProjectProcessor) this).getCurrentProject();
    }
    if (task != null && project != null) {
      String message = sysMessages_.getCommandXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task)
          .replace("<Z/>", project);
      return message;
    } else if (task != null) {
      String message = sysMessages_.getCommandXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task);
      return message;
    } else if (project != null) {
      String message = sysMessages_.getCommandXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", project);
      return message;
    } else {
      String message = sysMessages_.getCommandXIsStillRunning();
      message = message.replace("<X/>", brief_.getName());
      return message;
    }
  }
  
  private String getFacetCurrentLocation() {
    if (!running.get()) {
      if (settingUp.get()) {
        String message = sysMessages_.getFacetXIsStillSettingUp();
        message = message.replace("<X/>", brief_.getName());
        return message;
      }
    }
    String task = null;
    if (I_TaskProcessor.class.isAssignableFrom(this.getClass())) {
      task = ((I_TaskProcessor) this).getCurrentTask();
    }
    String project = null;
    if (I_ProjectProcessor.class.isAssignableFrom(this.getClass())) {
      project = ((I_ProjectProcessor) this).getCurrentProject();
    }
    if (task != null && project != null) {
      String message = sysMessages_.getFacetXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task)
          .replace("<Z/>", project);
      return message;
    } else if (task != null) {
      String message = sysMessages_.getFacetXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task);
      return message;
    } else if (project != null) {
      String message = sysMessages_.getFacetXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", project);
      return message;
    } else {
      String message = sysMessages_.getFacetXIsStillRunning();
      message = message.replace("<X/>", brief_.getName());
      return message;
    }
  }
  
  private String getTraitCurrentLocation() {
    if (!running.get()) {
      if (settingUp.get()) {
        String message = sysMessages_.getTraitXIsStillSettingUp();
        message = message.replace("<X/>", brief_.getName());
        return message;
      }
    }
    String task = null;
    if (I_TaskProcessor.class.isAssignableFrom(this.getClass())) {
      task = ((I_TaskProcessor) this).getCurrentTask();
    }
    String project = null;
    if (I_ProjectProcessor.class.isAssignableFrom(this.getClass())) {
      project = ((I_ProjectProcessor) this).getCurrentProject();
    }
    if (task != null && project != null) {
      String message = sysMessages_.getTraitXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task)
          .replace("<Z/>", project);
      return message;
    } else if (task != null) {
      String message = sysMessages_.getTraitXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", task);
      return message;
    } else if (project != null) {
      String message = sysMessages_.getTraitXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", project);
      return message;
    } else {
      String message = sysMessages_.getTraitXIsStillRunning();
      message = message.replace("<X/>", brief_.getName());
      return message;
    }
  }
}
