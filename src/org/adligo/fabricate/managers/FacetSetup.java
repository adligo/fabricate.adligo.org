package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.I_CommandAware;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_RepositoryManagerAware;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FacetSetup implements I_FacetSetup {
  private final I_FabSystem system_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  private final List<I_FabricationRoutine> routines_ = new ArrayList<I_FabricationRoutine>();
  private final I_RepositoryManager repositoryManager_;
  
  public FacetSetup(I_FabSystem system, RoutineFabricateFactory factory, I_RepositoryManager rm) {
    fabricate_ = factory.getFabricate();
    factory_ = factory;
    repositoryManager_ = rm;
    system_ = system;
  }
  
  public void clearRoutines() {
    routines_.clear();
  }
  
  public I_FabricationRoutine getRoutine(int i) {
    return routines_.get(i);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.managers.I_FacetSetup#processFacetSetup(java.lang.String)
   */
  @Override
  public I_FabricationRoutine processFacetSetup(String name) throws FabricationRoutineCreationException {
    Set<I_ExpectedRoutineInterface> es = Collections.emptySet();
    I_FabricationRoutine routine = factory_.createFacet(name,es);
    routines_.add(routine);
    
    routine.setSystem(system_);
    if (I_FabricateAware.class.isAssignableFrom(routine.getClass())) {
      ((I_FabricateAware) routine).setFabricate(fabricate_);
    } 
    I_RoutineFactory traitFactory = factory_.getFacets();
    I_RoutineFactory taskFactory = traitFactory.createTaskFactory(name);
    routine.setTaskFactory(taskFactory);
    routine.setTraitFactory(factory_.getTraits());
    if (I_CommandAware.class.isAssignableFrom(routine.getClass())) {
      I_RoutineFactory cmdFactory = factory_.getCommands();
      ((I_CommandAware) routine).setCommandFactory(cmdFactory);
    }
    if (I_RepositoryManagerAware.class.isAssignableFrom(routine.getClass())) {
      ((I_RepositoryManagerAware) routine).setRepositoryManager(repositoryManager_);
    }
    return routine;
  }
  
  public int size() {
    return routines_.size();
  }
}
