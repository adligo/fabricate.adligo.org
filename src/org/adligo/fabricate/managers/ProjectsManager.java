package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.implicit.ImplicitFacets;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;

public class ProjectsManager {
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  private final FacetExecutor executor_;
  private final FacetSetup setup_;
  
  public ProjectsManager(I_FabSystem system,  RoutineFabricateFactory factory, I_RepositoryManager rm) {
    log_ = system.getLog();
    constants_ = system.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    executor_ = new FacetExecutor(system, factory);
    setup_ = new FacetSetup(system, factory, rm);
  }
  
  public FailureType setupAndRun(FabricationMemoryMutant<Object> memory) {
    
    FailureType obtainFailure = runFacet(ImplicitFacets.OBTAIN_PROJECTS, memory);
    if (obtainFailure != null) {
      return obtainFailure;
    }
    setup_.clearRoutines();
    FailureType loadFailure = runFacet(ImplicitFacets.LOAD_PROJECTS, memory);
    if (loadFailure != null) {
      return loadFailure;
    }
    setup_.clearRoutines();
    FailureType downloadFailure = runFacet(ImplicitFacets.DOWNLOAD_DEPENDENCIES, memory);
    if (downloadFailure != null) {
      return downloadFailure;
    }
    setup_.clearRoutines();
    FailureType failure = runFacet(ImplicitFacets.SETUP_PROJECTS, memory);
    if (failure != null) {
      return failure;
    }
    return null;
  }

  private FailureType runFacet(String facet, FabricationMemoryMutant<Object> memory) {
    if (log_.isLogEnabled(CommandManager.class)) {
      String message = sysMessages_.getRunningFacetX();
      message = message.replace("<X/>", facet);
      log_.println(message);
    }
    return executor_.run(facet, setup_, memory);
  }

}
