package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_LibraryDependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;

import java.util.List;



/**
 * This will probably eventually contain
 * all information about a project, for now
 * it was a extract from Compile/Jar for
 * the FabricateClasspath2Eclipse command. 
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

  /**
   * A list is used to keep order correlated with
   * the project.xml file.
   * @return
   */
  public List<I_Dependency> getDependencies();
  
  /**
   * A list is used to keep order correlated with
   * the project.xml file.
   * @return
   */
  public List<I_LibraryDependency> getLibraryDependencies();
  /**
   * A list is used to keep order correlated with
   * the project.xml file.
   * @return
   */
  public List<I_ProjectDependency> getProjectDependencies();
  
}
