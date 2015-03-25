package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.Project;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.I_CommandAware;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_ProjectsAware;
import org.adligo.fabricate.routines.I_RepositoryManagerAware;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class StageSetup implements I_StageSetup {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  private final List<I_FabricationRoutine> routines_ = new ArrayList<I_FabricationRoutine>();
  private final I_RepositoryManager repositoryManager_;
  private List<I_Project> projects_ = new ArrayList<I_Project>();
  
  public StageSetup(I_FabSystem system, RoutineFabricateFactory factory, I_RepositoryManager rm) {
    fabricate_ = factory.getFabricate();
    factory_ = factory;
    repositoryManager_ = rm;
    system_ = system;
    log_ = system.getLog();
  }
  
  public void clearRoutines() {
    routines_.clear();
  }
  
  public I_FabricationRoutine getRoutine(int i) {
    return routines_.get(i);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.managers.I_StageSetup#processStageSetup(java.lang.String)
   */
  @Override
  public I_FabricationRoutine processStageSetup(String name) throws FabricationRoutineCreationException {
    Set<I_ExpectedRoutineInterface> es = Collections.emptySet();
    I_FabricationRoutine routine = factory_.createStage(name,es);
    if (log_.isLogEnabled(StageSetup.class)) {
      log_.println(StageSetup.class.getSimpleName() + ".processStageSetup created the following routine;" + routine);
    }
    routines_.add(routine);
    
    
    routine.setSystem(system_);
    if (I_FabricateAware.class.isAssignableFrom(routine.getClass())) {
      ((I_FabricateAware) routine).setFabricate(fabricate_);
    } 
    I_RoutineFactory stageFactory = factory_.getStages();
    I_RoutineFactory taskFactory = stageFactory.createTaskFactory(name);
    routine.setTaskFactory(taskFactory);
    
    routine.setTraitFactory(factory_.getTraits());
    if (log_.isLogEnabled(StageSetup.class)) {
      log_.println(StageSetup.class.getSimpleName() + ".processStageSetup setTraitFactory");
    }
    
    if (routine instanceof I_CommandAware) {
      I_RoutineFactory cmdFactory = factory_.getCommands();
      ((I_CommandAware) routine).setCommandFactory(cmdFactory);
    }
    if (routine instanceof I_RepositoryManagerAware) {
      ((I_RepositoryManagerAware) routine).setRepositoryManager(repositoryManager_);
    }
    if (routine instanceof I_ProjectsAware) {
      if (log_.isLogEnabled(StageSetup.class)) {
        log_.println(StageSetup.class.getSimpleName() + ".processStageSetup setProjects " + projects_.size());
      }
      ((I_ProjectsAware) routine).setProjects(projects_);
    }
    if (log_.isLogEnabled(StageSetup.class)) {
      log_.println(StageSetup.class.getSimpleName() + ".processStageSetup returning ");
    }
    return routine;
  }
  
  public int size() {
    return routines_.size();
  }

  public List<I_Project> getProjects() {
    return projects_;
  }

  public void setProjects(List<I_Project> projects) {
    projects_.clear();
    for (I_Project proj: projects) {
      if (proj instanceof Project) {
        projects_.add(proj);
      } else {
        projects_.add(new Project(proj));
      }
    }
  }
}
