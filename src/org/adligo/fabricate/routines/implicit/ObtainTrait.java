package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;

/**
 * This class just contains the routine brief
 * for the obtain trait.
 * @author scott
 *
 */
public class ObtainTrait extends ProjectBriefQueueRoutine {
  public static final String NAME = "obtain";
  public static final RoutineBrief ROUTINE_BRIEF = getRoutineBrief();
  
  private static RoutineBrief getRoutineBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(ScmTrait.class);
    rbm.setName(NAME);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT);
    
    RoutineBriefMutant clone = new RoutineBriefMutant();
    clone.setClazz(GitCloneRoutine.class);
    clone.setName("clone");
    clone.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT_TASK);
    
    rbm.addNestedRoutine(clone);
    
    return new RoutineBrief(rbm);
  }
  

  
}
