package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class populates routines with anything they might need.
 * 
 * @author scott
 *
 */
public class RoutinePopulatorMutant implements I_RoutinePopulatorMutant {
 
  private final I_Fabricate fabricate_;
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private List<I_Project> projects_;
  private final I_RoutineFabricateProcessorFactory factory_;
  private final I_SystemMessages sysMessages_;
  
  private I_RoutineFactory taskFactory_;
  private I_RepositoryManager repositoryManager_;
  private I_RepositoryFactory repositoryFactory_;
  private I_RoutineProcessorFactory routineProcessorFactory_;
  /**
   * objects to pass to I_InputAware
   */
  private Map<String,Object> input_ = new HashMap<String,Object>();
  
  public RoutinePopulatorMutant(I_FabSystem system, I_RoutineFabricateProcessorFactory factory) {
    fabricate_ = factory.getFabricate();
    system_ = system;
    log_ = system.getLog();
    I_FabricateConstants constants = system.getConstants();
    sysMessages_ = constants.getSystemMessages();
    factory_ = factory;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#populate(org.adligo.fabricate.models.common.I_FabricationRoutine)
   */
  @Override
  @SuppressWarnings("unchecked")
  public void populate(I_FabricationRoutine routine) {
    //alpha order 
    if (routine instanceof I_CommandAware) {
      I_RoutineFactory cmdFactory = factory_.getCommands();
      ((I_CommandAware) routine).setCommandFactory(cmdFactory);
    }
    if (routine instanceof I_FabricateAware) {
      ((I_FabricateAware) routine).setFabricate(fabricate_);
    } 
    if (routine instanceof I_RepositoryManagerAware) {
      if (repositoryManager_ == null) {
        throwException(I_RepositoryManagerAware.class.getName(), routine);
      }
      ((I_RepositoryManagerAware) routine).setRepositoryManager(repositoryManager_);
    }
    if (routine instanceof I_RoutinePopulatorAware) {
      ((I_RoutinePopulatorAware) routine).setRoutinePopluator(this);
    }
    if (routine instanceof I_RepositoryFactoryAware) {
      if (repositoryFactory_ == null) {
        throwException(I_RepositoryFactoryAware.class.getName(), routine);
      }
      ((I_RepositoryFactoryAware) routine).setRepositoryFactory(repositoryFactory_);
    }
    if (routine instanceof I_ProjectsAware) {
      if (projects_ == null) {
        throwException(I_ProjectsAware.class.getName(), routine);
      }
      ((I_ProjectsAware) routine).setProjects(projects_);
    }
    if (routine instanceof I_RoutineProcessorFactoryAware) {
      if (routineProcessorFactory_ == null) {
        throwException(I_RoutineProcessorFactory.class.getName(), routine);
      }
      ((I_RoutineProcessorFactoryAware) routine).setRoutineFabricateFactory(
          routineProcessorFactory_);
    }
    if (routine instanceof I_InputAware) {
      addInputAware((I_InputAware<Object>) routine);
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#getRepositoryManager()
   */
  @Override
  public I_RepositoryManager getRepositoryManager() {
    return repositoryManager_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#getRepositoryFactory()
   */
  @Override
  public I_RepositoryFactory getRepositoryFactory() {
    return repositoryFactory_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#getProjects()
   */
  @Override
  public List<I_Project> getProjects() {
    return new ArrayList<I_Project>(projects_);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#getRoutineProcessorFactory()
   */
  @Override
  public I_RoutineProcessorFactory getRoutineProcessorFactory() {
    return routineProcessorFactory_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluatorMutant#putInput(java.lang.Class, java.lang.Object)
   */
  @Override
  public void putInput(Class<?> clazz, Object input) {
    input_.put(clazz.getName(), input);
  }
  
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluatorMutant#setProjects(java.util.List)
   */
  @Override
  public void setProjects(List<I_Project> projects) {
    if (projects != null) {
      if (projects_ == null) {
        projects_ = new ArrayList<I_Project>();
      }
      projects_.clear();
      
      for (I_Project project: projects) {
        if (project != null) {
          projects_.add(project);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluatorMutant#setRepositoryFactory(org.adligo.fabricate.repository.I_RepositoryFactory)
   */
  @Override
  public void setRepositoryFactory(I_RepositoryFactory repositoryFactory) {
    this.repositoryFactory_ = repositoryFactory;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#setRepositoryManager(org.adligo.fabricate.repository.I_RepositoryManager)
   */
  @Override
  public void setRepositoryManager(I_RepositoryManager repositoryManager) {
    this.repositoryManager_ = repositoryManager;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#setRoutineProcessorFactory(org.adligo.fabricate.routines.I_RoutineProcessorFactory)
   */
  @Override
  public void setRoutineProcessorFactory(I_RoutineProcessorFactory routineProcessorFactory) {
    this.routineProcessorFactory_ = routineProcessorFactory;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#getTaskFactory()
   */
  @Override
  public I_RoutineFactory getTaskFactory() {
    return taskFactory_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.I_RoutinePopluator#setTaskFactory(org.adligo.fabricate.models.common.I_RoutineFactory)
   */
  @Override
  public void setTaskFactory(I_RoutineFactory taskFactory) {
    this.taskFactory_ = taskFactory;
  }
  

  private void addInputAware(I_InputAware<Object> inputAware) {
    if (log_.isLogEnabled(RoutinePopulatorMutant.class)) {
      log_.println("the following class is input aware " + system_.lineSeparator() +
          inputAware.getClass().getName());
    }
    List<Class<?>> inputClasses = inputAware.getClassType(I_InputAware.class);
    if (inputClasses.size() != 1) {
      String messages = sysMessages_.getTheFollowingRoutineImplementsXHoweverItsGetClassTypeMethodReturnedYClassTypesInsteadOfOne();
      messages = messages.replace("<X/>", I_InputAware.class.getName());
      messages = messages.replace("<Y/>", "" + inputClasses.size());
      throw new IllegalStateException(messages + system_.lineSeparator() +
          inputAware.getClass().getName());
    }
    Class<?> clazz = inputClasses.get(0);
    if (clazz == null) {
      String messages = sysMessages_.getTheFollowingRoutineImplementsXButTheRoutinesPopulatorValueIsNull();
      messages = messages.replace("<X/>", I_InputAware.class.getName());
      throw new IllegalStateException(messages + system_.lineSeparator() +
          inputAware.getClass().getName());
    }
    String clazzName = clazz.getName();
    Object in = input_.get(clazzName);
    if (in != null) {
      if (repositoryManager_ == null) {
        throwException(I_InputAware.class.getName() + 
            "<" + clazz.getSimpleName() + ">", inputAware);
      }
    }
    if (log_.isLogEnabled(RoutinePopulatorMutant.class)) {
      log_.println("setting input on " + system_.lineSeparator() +
          inputAware.getClass().getName() + system_.lineSeparator() +
          in);
    }
    inputAware.setInput(in);
  }
  

  private void throwException(String x, I_FabricationRoutine routine) {
    String messages = sysMessages_.getTheFollowingRoutineImplementsXButTheRoutinesPopulatorValueIsNull();
    messages = messages.replace("<X/>", x);
    throw new IllegalStateException(messages + system_.lineSeparator() +
        routine.getClass().getName());
  }
}
