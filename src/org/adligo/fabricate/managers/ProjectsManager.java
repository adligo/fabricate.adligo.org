package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.FailureTransport;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.routines.implicit.ImplicitFacets;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;

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
  
  public FailureTransport setupAndRun(FabricationMemoryMutant<Object> memory) {
    
    FailureTransport obtainFailure = runFacet(ImplicitFacets.OBTAIN_PROJECTS, memory);
    if (obtainFailure != null) {
      return obtainFailure;
    }
    logSuccess(ImplicitFacets.OBTAIN_PROJECTS);
    setup_.clearRoutines();
    FailureTransport loadFailure = runFacet(ImplicitFacets.LOAD_PROJECTS, memory);
    if (loadFailure != null) {
      return loadFailure;
    }
    logSuccess(ImplicitFacets.LOAD_PROJECTS);
    setup_.clearRoutines();
    FailureTransport downloadFailure = runFacet(ImplicitFacets.DOWNLOAD_DEPENDENCIES, memory);
    if (downloadFailure != null) {
      return downloadFailure;
    }
    logSuccess(ImplicitFacets.DOWNLOAD_DEPENDENCIES);
    setup_.clearRoutines();
    FailureTransport failure = runFacet(ImplicitFacets.SETUP_PROJECTS, memory);
    if (failure != null) {
      return failure;
    }
    logSuccess(ImplicitFacets.SETUP_PROJECTS);
    return null;
  }

  private void logSuccess(String p) {
    if (log_.isLogEnabled(ProjectsManager.class)) {
      String message = sysMessages_.getFacetXCompletedSuccessfully();
      message = message.replace("<X/>", p);
      log_.println(message);
    }
  }

  private FailureTransport runFacet(String facet, FabricationMemoryMutant<Object> memory) {
    if (log_.isLogEnabled(CommandManager.class)) {
      String message = sysMessages_.getRunningFacetX();
      message = message.replace("<X/>", facet);
      log_.println(message);
    }
    return executor_.run(facet, setup_, memory);
  }

}
