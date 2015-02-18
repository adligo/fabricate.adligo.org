package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_RoutineBrief;

import java.util.Map;

public interface I_DefaultRoutines {
   public Map<String,I_RoutineBrief> getImplicitTraits();
   public Map<String,I_RoutineBrief> getImplicitCommands();
   public Map<String,I_RoutineBrief> getImplicitStages();
}
