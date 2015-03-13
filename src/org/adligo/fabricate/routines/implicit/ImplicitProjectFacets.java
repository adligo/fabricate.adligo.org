package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;

/**
 * This class just contains the routine brief
 * for the obtain trait.
 * @author scott
 *
 */
public class ImplicitProjectFacets {
  public static final String CLONE = "clone";
  public static final String CHECKOUT = "checkout";
  public static final String UPDATE = "update";
  public static final String LOAD = "load";
  public static final String NAME = "obtain";
  public static final RoutineBrief OBTAIN_BRIEF = getRoutineBrief();
  
  private static RoutineBrief getRoutineBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(ScmRoutine.class);
    rbm.setName(NAME);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET);
    
    RoutineBriefMutant clone = new RoutineBriefMutant();
    clone.setClazz(GitCloneRoutine.class);
    clone.setName(CLONE);
    clone.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    
    rbm.addNestedRoutine(clone);
    
    RoutineBriefMutant update = new RoutineBriefMutant();
    update.setClazz(GitUpdateRoutine.class);
    update.setName(UPDATE);
    update.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    
    rbm.addNestedRoutine(update);
    
    RoutineBriefMutant checkout = new RoutineBriefMutant();
    checkout.setClazz(GitCloneRoutine.class);
    checkout.setName(CHECKOUT);
    checkout.setOrigin(RoutineBriefOrigin.IMPLICIT_FACET_TASK);
    
    rbm.addNestedRoutine(checkout);
    
    return new RoutineBrief(rbm);
  }
  

  
}
