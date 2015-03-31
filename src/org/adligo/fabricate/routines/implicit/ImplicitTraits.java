package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.routines.ProjectQueueRoutine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImplicitTraits {
  public static final String GIT_STATUS = "git status";
  public static final String GIT_PROJECT_STATUS = "git project status";
  public static final I_RoutineBrief GIT_STATUS_BRIEF = getGitStatusBrief();
  
  public static final List<I_RoutineBrief> ALL = getAll();
  
  private static List<I_RoutineBrief> getAll() {
    List<I_RoutineBrief> ret = new ArrayList<I_RoutineBrief>();
    
    try {
      ret.add(new RoutineBrief(EncryptTrait.NAME, 
          EncryptTrait.class.getName(), RoutineBriefOrigin.IMPLICIT_TRAIT));
      ret.add(new RoutineBrief(DecryptTrait.NAME, 
          DecryptTrait.class.getName(), RoutineBriefOrigin.IMPLICIT_TRAIT));
      ret.add(new RoutineBrief(NameJarTrait.NAME, 
          NameJarTrait.class.getName(), RoutineBriefOrigin.IMPLICIT_TRAIT));
      ret.add(new RoutineBrief(FindSrcTrait.NAME, 
          FindSrcTrait.class.getName(), RoutineBriefOrigin.IMPLICIT_TRAIT));
      ret.add(GIT_STATUS_BRIEF);
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
    return Collections.unmodifiableList(ret);
  }
  
  private static I_RoutineBrief getGitStatusBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(ProjectQueueRoutine.class);
    rbm.setName(GIT_STATUS);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT);
    
    RoutineBriefMutant status = new RoutineBriefMutant();
    status.setClazz(GitStageTask.class);
    status.setName(GIT_PROJECT_STATUS);
    status.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT_TASK);
    rbm.addNestedRoutine(status);
    
    return new RoutineBrief(rbm);
  }
}
