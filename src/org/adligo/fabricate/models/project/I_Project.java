package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.scm.I_Scm;

import java.util.List;



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

  public List<I_Dependency> getDependencies();
  public List<I_ProjectDependency> getProjectDependencies();
  
  public I_Scm getScm();
}
