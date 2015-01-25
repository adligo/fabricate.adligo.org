package org.adligo.fabricate.models.project;



/**
 * This will probably eventually contain
 * all information about a project, for now
 * it was a extract from Compile/Jar for
 * the FabricateClasspath2Eclipse command. 
 *   Eventually this will probably contain
 * a lot of information about the project/
 * fabricate run.
 * 
 * @author scott
 */
public interface I_Project extends I_ProjectBrief {
  /**
   * This is the system dependent absolute path 
   * of the project.
   * @return
   */
  public String getDir();
  public String getShortName();
  public String getDomainName();
}
