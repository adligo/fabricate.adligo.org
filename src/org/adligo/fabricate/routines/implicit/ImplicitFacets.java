package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class just contains the routine brief
 * for the obtain trait.
 * @author scott
 *
 */
public class ImplicitFacets {
  
  public static final String SETUP_PROJECTS = "setup projects";
  public static final RoutineBrief SETUP_PROJECTS_BRIEF = getSetupProjectsBrief();
  
  public static final String CLONE_PROJECT_TASK = "clone project";
  public static final String CHECKOUT_PROJECT_TASK = "checkout project";
  public static final String DOWNLOAD_DEPENDENCIES = "download dependencies";
  public static final RoutineBrief DOWNLOAD_DEPENDENCIES_BRIEF = getDownloadDependenciesBrief();
  
  public static final String UPDATE_PROJECT_TASK = "update project";
  public static final String LOAD_PROJECTS = "load projects";
  public static final String LOAD_PROJECT_TASK = "load project";
  public static final RoutineBrief LOAD_PROJECTS_BRIEF = getProjectsLoadBrief();
  public static final String OBTAIN_PROJECTS = "obtain projects";
  public static final RoutineBrief OBTAIN_BRIEF = getObtainBrief();
  /**
   * all must be at the bottom, so that instances can be init
   */
  public static final List<I_RoutineBrief> ALL = getAll();
  
  
  private static List<I_RoutineBrief> getAll() {
    List<I_RoutineBrief> ret = new ArrayList<I_RoutineBrief>();
    
    try {
      ret.add(SETUP_PROJECTS_BRIEF);
      ret.add(OBTAIN_BRIEF);    
      ret.add(LOAD_PROJECTS_BRIEF);
      ret.add(DOWNLOAD_DEPENDENCIES_BRIEF);
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
    return Collections.unmodifiableList(ret);
  }
  
  private static RoutineBrief getSetupProjectsBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(SetupProjectsRoutine.class);
    rbm.setName(SETUP_PROJECTS);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET);
    
    return new RoutineBrief(rbm);
  }
  
  private static RoutineBrief getProjectsLoadBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(ProjectBriefQueueRoutine.class);
    rbm.setName(LOAD_PROJECTS);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET);
    
    RoutineBriefMutant load = new RoutineBriefMutant();
    load.setClazz(LoadProjectTask.class);
    load.setName(LOAD_PROJECT_TASK);
    load.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    rbm.addNestedRoutine(load);
    
    return new RoutineBrief(rbm);
  }
  
  private static RoutineBrief getDownloadDependenciesBrief() {
    RoutineBriefMutant download = new RoutineBriefMutant();
    download.setClazz(DownloadDependenciesRoutine.class);
    download.setName(DOWNLOAD_DEPENDENCIES);
    download.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    return new RoutineBrief(download);
  }
  
  
  private static RoutineBrief getObtainBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(ScmRoutine.class);
    rbm.setName(OBTAIN_PROJECTS);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET);
    
    RoutineBriefMutant clone = new RoutineBriefMutant();
    clone.setClazz(GitCloneRoutine.class);
    clone.setName(CLONE_PROJECT_TASK);
    clone.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    
    rbm.addNestedRoutine(clone);
    
    RoutineBriefMutant update = new RoutineBriefMutant();
    update.setClazz(GitUpdateRoutine.class);
    update.setName(UPDATE_PROJECT_TASK);
    update.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    
    rbm.addNestedRoutine(update);
    
    RoutineBriefMutant checkout = new RoutineBriefMutant();
    checkout.setClazz(GitCloneRoutine.class);
    checkout.setName(CHECKOUT_PROJECT_TASK);
    checkout.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    
    rbm.addNestedRoutine(checkout);
    
    return new RoutineBrief(rbm);
  }
  

  
}
