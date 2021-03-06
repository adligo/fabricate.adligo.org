package org.adligo.fabricate.common;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.I_AttributesContainer;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

/**
 * This interface represents the 
 * layout of files and other settings
 * used by the tasks to determine how they should
 * execute.  
 * @author scott
 *
 */
public interface I_RunContext {
  public FabRunType getRunType();
  public I_FabLog getLog();
  public I_FabFileIO getFileIO();
  public I_FabXmlFileIO getXmlFileIO();
  public boolean hasArg(String key);
  public String getArgValue(String key);
  public String getFabricateDirPath();
  public String getFabricateXmlPath();
  public String getFabricateVersion();
  
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
  public String getJavaHome();
  public String getJavaVersion();
  
  public String getLocalRepositoryPath();
  /**
   * This is the location where the 
   * output goes (result.xml, test xml files exc).
   * @return
   */
  public String getOutputPath();
  
  /**
   * @deprecated
   * remove this because it depends on jaxb 
   * generated classes.  Replace usages
   * with FabContextMutant instead of this interface,
   * so it can still be used locally in this project, 
   * wrap other methods in FabricateType to this class.
   * @return
   */
  public FabricateType getFabricate();
  
  /**
   * @deprecated
   * please use the getProjectContext Method
   * @return
   */
  public FabricateProjectType getProject();
  /**
   * This is the project where the
   * fabricate execution started,
   * or the name of the fabricate project from the command line;
   * fab dev project="css.adligo.org".  When 
   * the project context is present it implies that only this project
   * should be fabricated (with out fabricating it's project dependencies).
   * 
   * @return
   */
  public I_AttributesContainer getProjectContext();
  
  public void putInMemory(String key, Object value);
  public Object getFromMemory(String key);
  public I_FabricateConstants getConstants();
  
}
