package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.implicit.ImplicitFacets;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

public class ProjectsManager {
  private final FacetExecutor executor_;
  private final FacetSetup setup_;
  
  public ProjectsManager(I_FabSystem system,  RoutineFabricateFactory factory, I_RepositoryManager rm) {
    executor_ = new FacetExecutor(system, factory);
    setup_ = new FacetSetup(system, factory, rm);
  }
  
  public FailureType setupAndRun(FabricationMemoryMutant<Object> memory) {
    FailureType failure = executor_.run(ImplicitFacets.OBTAIN_PROJECTS, setup_, memory);
    if (failure != null) {
      return failure;
    }
    setup_.clearRoutines();
    FailureType loadFailure = executor_.run(ImplicitFacets.LOAD_PROJECTS, setup_, memory);
    if (loadFailure != null) {
      return loadFailure;
    }
    setup_.clearRoutines();
    FailureType downloadFailure = executor_.run(ImplicitFacets.DOWNLOAD_DEPENDENCIES, setup_, memory);
    if (downloadFailure != null) {
      return downloadFailure;
    }
    return null;
  }

  

}
