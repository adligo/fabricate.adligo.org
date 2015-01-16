package org.adligo.fabricate.models;

/**
 * This is fundamentally a immutable
 * org.adligo.fabricate.xml.io.tasks.v1_0.ParamType
 * Note the fabricate param's are merged with the project
 * before getting passed to the plug-in for commands
 * and stage plug-ins.
 * @author scott
 *
 */
public interface I_ParamsTree {
  public String getKey();
  public String getValue();
  public I_ParamsTree getParams();
}
