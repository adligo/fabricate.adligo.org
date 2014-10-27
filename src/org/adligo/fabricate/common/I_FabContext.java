package org.adligo.fabricate.common;

/**
 * This interface represents the 
 * layout of files and other settings
 * used by the tasks to determine how they should
 * execute.  
 * @author scott
 *
 */
public interface I_FabContext {
  public FabRunType getRunType();
  public boolean isLogEnabled(Class<?> clazz);
  public boolean hasArg(String key);
  public String getArgValue(String key);
  public String getFabricateXmlPath();
  public String getFabricateDirPath();
  /**
   * this is only used during a development
   * run of a single project
   * @return
   */
  public String getProjectPath();
  /**
   * this is used during a default or development
   * run of a project group
   * @return
   */
  public String getProjectsPath();
  /**
   * this is used for a aggergrate run of
   * multiple project groups
   * @return
   */
  public String getProjectGroupsPath();
  public I_Depot getDepot();
  /**
   * This is the location where the fab
   * script was executed.
   * @return
   */
  public String getInitialPath();
  /**
   * This is the location where the 
   * output goes (result.xml, test xml files exc).
   * @return
   */
  public String getOutputPath();
  
}
