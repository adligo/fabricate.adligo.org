package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_RoutineBrief {
  public Class<? extends I_FabricationRoutine> getClazz();

  public String getName();

  public RoutineBriefOrigin getOrigin();

  public List<I_Parameter> getParameters();
  /**
   * Return the first parameter only
   * @return
   */
  public String getParameter(String key);
  public List<String> getParameters(String key);
  
  public List<I_RoutineBrief> getNestedRoutines();
  public I_RoutineBrief getNestedRoutine(String name);
 
  public boolean isOptional();
  
  public boolean hasParameter(String key);
}