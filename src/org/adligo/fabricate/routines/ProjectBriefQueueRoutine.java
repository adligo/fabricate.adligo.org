package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;

/**
 * This class provides a Queue of projects which can help implementers 
 * process projects concurrently in any order.
 * 
 * @diagram_sync on 1/26/2014 with Overview.seq
 * @author scott
 *
 */
public abstract class ProjectBriefQueueRoutine extends AbstractRoutine 
  implements I_ConcurrencyAware, I_FabricationRoutine, I_ProjectsAware {
 
}
