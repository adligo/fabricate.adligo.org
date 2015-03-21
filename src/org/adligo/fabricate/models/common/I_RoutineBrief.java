package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_RoutineBrief {
  public Class<? extends I_FabricationRoutine> getClazz();

  public String getName();

  public RoutineBriefOrigin getOrigin();

  /**
   * Return the first parameter which matches the key only
   * or null.
   * @param key
   * @return
   */
  public I_Parameter getParameter(String key);
  
  public List<I_Parameter> getParameters();
  /**
   * Return the first parameter only
   * @return
   */
  public String getParameterValue(String key);
  /**
   * Return the List of parameters that match the key;
   * @param key
   * @return
   */
  public List<I_Parameter> getParameters(String key);
  
  /**
   * Return the List of values for top level parameters
   * with the key parameter;
   * @param key
   * @return
   */
  public List<String> getParameterValues(String key);
  
  public List<I_RoutineBrief> getNestedRoutines();
  public I_RoutineBrief getNestedRoutine(String name);
 
  public boolean isCommand();
  public boolean isOptional();
  public boolean isStage();
  public boolean isArchivalStage();
  
  public boolean hasParameter(String key);
}