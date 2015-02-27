package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;

import java.util.Collection;
import java.util.List;

public class UpdateTrait extends ProjectBriefQueueRoutine {
  public static final String NAME = "update";
  public static final RoutineBrief ROUTINE_BRIEF = getRoutineBrief();
  
  private static RoutineBrief getRoutineBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(UpdateTrait.class);
    rbm.setName(NAME);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT);
    
    RoutineBriefMutant clone = new RoutineBriefMutant();
    clone.setClazz(GitPullRoutine.class);
    clone.setName("pull");
    clone.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT_TASK);
    
    rbm.addNestedRoutine(clone);
    
    return new RoutineBrief(rbm);
  }
  

  
}
