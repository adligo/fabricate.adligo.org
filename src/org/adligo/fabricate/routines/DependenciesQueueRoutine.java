package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;

/**
 * This class provides a Queue of projects which can help implementers 
 * process projects concurrently in project dependency order which is
 * fairly tricky.
 * 
 * @diagram_sync on 1/26/2014 with Overview.seq
 * @author scott
 *
 */
public abstract class DependenciesQueueRoutine implements 
  I_ConcurrencyAware, I_FabricationRoutine, I_ProjectsAware {

}
