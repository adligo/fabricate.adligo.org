package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImplicitArchiveStages {
  public static final List<I_RoutineBrief> ALL = getAll();
  
  private static List<I_RoutineBrief> getAll() {
    List<I_RoutineBrief> ret = new ArrayList<I_RoutineBrief>();
    
    return Collections.unmodifiableList(ret);
  }
}
