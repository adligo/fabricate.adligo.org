package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_AttributeConstants;
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
import org.adligo.fabricate.repository.I_RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRoutine implements I_FabricationRoutine, 
  I_RepositoryFactoryAware, I_RoutinePopulatorAware {
  protected I_AttributeConstants attribConstants_;
  protected I_RoutineBrief brief_;
  protected I_FabSystem system_;
  protected I_FabFileIO files_;
  protected I_FabXmlFileIO xmlFiles_;
  protected I_FabricateConstants constants_;
  protected I_CommandLineConstants cmdConstants_;
  protected I_SystemMessages sysMessages_;
  protected I_RepositoryFactory repositoryFactory_;
  protected I_RoutinePopulator routinePopluator_;
  protected final RoutineLocationInfo locationInfo_ = new RoutineLocationInfo();
  
  protected I_ImplicitTraitMessages implicit_;
  protected I_FabLog log_;
  protected I_RoutineFactory traitFactory_;
  protected I_RoutineFactory taskFactory_;
  protected I_FabricateXmlDiscovery locations_;
  private AtomicBoolean settingUp = new AtomicBoolean(false);
  private AtomicBoolean running = new AtomicBoolean(false);
  
  @Override
  public String getAdditionalDetail() {
    return null;
  }
  
  @Override
  public I_RoutineBrief getBrief() {
    return brief_;
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
      case IMPLICIT_ARCHIVE_STAGE:
      case PROJECT_ARCHIVE_STAGE:
        return getArchiveCurrentLocation();
      default:
    }
    return AbstractRoutine.class.getName() + ".getCurrentLocation() unknown location for " + rbo;
  }
  
  @Override
  public I_FabricateXmlDiscovery getLocations() {
    return locations_;
  }
  
  @Override
  public I_RepositoryFactory getRepositoryFactory() {
    return repositoryFactory_;
  }
  
  @Override
  public I_RoutinePopulator getRoutinePopluator() {
    return routinePopluator_;
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
  
  public void makeDir(String dir) {
    if (!files_.mkdirs(dir)) {
      String message = sysMessages_.getThereWasAProblemCreatingTheFollowingDirectory();
      message = message + system_.lineSeparator() + dir;
      throw new RuntimeException(message);
    }
  }
  
  public List<Class<?>> newClassList(Class<?> ... c) {
    List<Class<?>> ret = new ArrayList<Class<?>>();
    for (int i = 0; i < c.length; i++) {
      ret.add(c[i]);
    }
    return ret;
  }
  
  @Override
  public void run() {
    setRunning();
  }
 
  public void setBrief(I_RoutineBrief brief) {
    this.brief_ = brief;
  }
  
  @Override
  public void setLocations(I_FabricateXmlDiscovery locations) {
    locations_ = locations;
  }
  
  @Override
  public void setRepositoryFactory(I_RepositoryFactory repositoryFactory) {
    this.repositoryFactory_ = repositoryFactory;
  }
  
  @Override
  public void setRoutinePopluator(I_RoutinePopulator routinePopulator) {
    this.routinePopluator_ = routinePopulator;
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
    attribConstants_ = constants_.getAttributeConstants();
  }
  
  @Override
  public void setTaskFactory(I_RoutineFactory factory) {
    taskFactory_ = factory;
  }
  
  @Override
  public void setTraitFactory(I_RoutineFactory factory) {
    traitFactory_ = factory;
  }


  

 


  
  /**
   * /**
   * Overrides of this method should call this method.
   */
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory) throws FabricationRoutineCreationException {
    settingUp.set(true);
  }
  
  /**
   * Do nothing, allow extension classes to override.
   */
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(AbstractRoutine.class)) {
      log_.println(AbstractRoutine.class.getName() + " setup(I_FabricationMemoryMutant, I_RoutineMemoryMutant)");
    }
    settingUp.set(true);
    return true;
  }
  
  /**
   * sub classes may override
   */
  public void writeToMemory(I_FabricationMemoryMutant<Object> memory) {}
  
  /**
   * Overrides of this method should call this method first before they
   * do anything else.
   */
  protected void setRunning() {
    running.set(true);
  }
  
  private String getArchiveCurrentLocation() {
    if (!running.get()) {
      if (settingUp.get()) {
        String message = sysMessages_.getArchiveStageXIsStillSettingUp();
        message = message.replace("<X/>", brief_.getName());
        return message;
      }
    }
    String currentTask = locationInfo_.getCurrentTask();
    String currentProject = locationInfo_.getCurrentProject();
    
    if (currentTask != null && currentProject != null) {
      String message = sysMessages_.getArchiveStageXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask)
          .replace("<Z/>", currentProject);
      return message;
    } else if (currentTask != null) {
      String message = sysMessages_.getArchiveStageXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask);
      return message;
    } else if (currentProject != null) {
      String message = sysMessages_.getArchiveStageXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", currentProject);
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

    String currentTask = locationInfo_.getCurrentTask();
    String currentProject = locationInfo_.getCurrentProject();
    if (log_.isLogEnabled(AbstractRoutine.class)) {
      log_.println("got currentTask '" + currentTask + "' currentProject '" + 
          currentProject + "' on locationInfo " + locationInfo_.toString() + system_.lineSeparator() +
          this.toString());
    }
    if (currentTask != null && currentProject != null) {
      String message = sysMessages_.getBuildStageXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask)
          .replace("<Z/>", currentProject);
      return message;
    } else if (currentTask != null) {
      String message = sysMessages_.getBuildStageXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask);
      return message;
    } else if (currentProject != null) {
      String message = sysMessages_.getBuildStageXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", currentProject);
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

    String currentTask = locationInfo_.getCurrentTask();
    String currentProject = locationInfo_.getCurrentProject();
    if (currentTask != null && currentProject != null) {
      String message = sysMessages_.getCommandXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask)
          .replace("<Z/>", currentProject);
      return message;
    } else if (currentTask != null) {
      String message = sysMessages_.getCommandXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask);
      return message;
    } else if (currentProject != null) {
      String message = sysMessages_.getCommandXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", currentProject);
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
    
    String currentTask = locationInfo_.getCurrentTask();
    String currentProject = locationInfo_.getCurrentProject();
    
    if (currentTask != null && currentProject != null) {
      String message = sysMessages_.getFacetXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask)
          .replace("<Z/>", currentProject);
      return message;
    } else if (currentTask != null) {
      String message = sysMessages_.getFacetXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask);
      return message;
    } else if (currentProject != null) {
      String message = sysMessages_.getFacetXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", currentProject);
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
    String currentTask = locationInfo_.getCurrentTask();
    String currentProject = locationInfo_.getCurrentProject();
    
    if (currentTask != null && currentProject != null) {
      String message = sysMessages_.getTraitXTaskYIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask)
          .replace("<Z/>", currentProject);
      return message;
    } else if (currentTask != null) {
      String message = sysMessages_.getTraitXTaskYIsStillRunning();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Y/>", currentTask);
      return message;
    } else if (currentProject != null) {
      String message = sysMessages_.getTraitXIsStillRunningOnProjectZ();
      message = message.replace("<X/>", brief_.getName())
          .replace("<Z/>", currentProject);
      return message;
    } else {
      String message = sysMessages_.getTraitXIsStillRunning();
      message = message.replace("<X/>", brief_.getName());
      return message;
    }
  }

}
