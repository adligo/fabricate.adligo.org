package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImplicitTraits {
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
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
    return Collections.unmodifiableList(ret);
  }
}
