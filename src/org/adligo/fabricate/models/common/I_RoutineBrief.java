package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_RoutineBrief {
  public String getName();

  public Class<? extends I_FabricationRoutine> getClazz();

  public boolean isOptional();
  
  public RoutineBriefOrigin getOrigin();

  public List<I_Parameter> getParameters();
  
  public List<I_RoutineBrief> getNestedRoutines();

}