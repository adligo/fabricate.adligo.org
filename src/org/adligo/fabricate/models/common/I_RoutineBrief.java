package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_RoutineBrief {
  public String getName();

  public Class<? extends I_FabricationRoutine> getClazz();

  public boolean isOptional();
  
  public RoutineBriefOrigin getOrigin();

  public List<I_Parameter> getParameters();
  public String getParameter(String key);
  
  public List<I_RoutineBrief> getNestedRoutines();
  public I_RoutineBrief getNestedRoutine(String name);
}