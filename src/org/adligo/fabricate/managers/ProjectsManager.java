package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.I_FailureTransport;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineExecutor;
import org.adligo.fabricate.routines.I_RoutineFabricateFactory;
import org.adligo.fabricate.routines.implicit.ImplicitFacets;

public class ProjectsManager {
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  private final I_RoutineBuilder routineBuilder_;
  private final I_RoutineFabricateFactory factory_;
  
  public ProjectsManager(I_FabSystem system,  I_RoutineFabricateFactory factory, 
      I_RoutineBuilder routineBuilder) {
    log_ = system.getLog();
    constants_ = system.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    routineBuilder_ = routineBuilder;
    factory_ = factory;
  }
  
  public I_FailureTransport setupAndRun(FabricationMemoryMutant<Object> memory) {
    
    I_FailureTransport obtainFailure = runFacet(ImplicitFacets.OBTAIN_PROJECTS, memory);
    if (obtainFailure != null) {
      return obtainFailure;
    }
    logSuccess(ImplicitFacets.OBTAIN_PROJECTS);
    I_FailureTransport loadFailure = runFacet(ImplicitFacets.LOAD_PROJECTS, memory);
    if (loadFailure != null) {
      return loadFailure;
    }
    logSuccess(ImplicitFacets.LOAD_PROJECTS);
    I_FailureTransport downloadFailure = runFacet(ImplicitFacets.DOWNLOAD_DEPENDENCIES, memory);
    if (downloadFailure != null) {
      return downloadFailure;
    }
    logSuccess(ImplicitFacets.DOWNLOAD_DEPENDENCIES);
    I_FailureTransport failure = runFacet(ImplicitFacets.SETUP_PROJECTS, memory);
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

  private I_FailureTransport runFacet(String facet, FabricationMemoryMutant<Object> memory) {
    if (log_.isLogEnabled(CommandManager.class)) {
      String message = sysMessages_.getRunningFacetX();
      message = message.replace("<X/>", facet);
      log_.println(message);
    }
    I_RoutineExecutor executor = factory_.createRoutineExecutor();
    
    return executor.run(facet, routineBuilder_, memory);
  }

}
