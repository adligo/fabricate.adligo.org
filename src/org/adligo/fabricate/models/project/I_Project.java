package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_LibraryDependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;

import java.util.List;
import java.util.Map;



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
   * Get the attributes that came from the project.xml file.
   * @return
   */
  public abstract List<I_Parameter> getAttributes();
  
  /**
   * Get the command that came from the project.xml file,
   * with the specified name.
   * @return
   */
  public abstract I_RoutineBrief getCommand(String name);
  
  /**
   * Get the commands that came from the project.xml file.
   * @return
   */
  public abstract Map<String, I_RoutineBrief> getCommands();
  
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
   * Normalized dependencies include actual dependencies from
   * the project.xml file and dependencies which were in 
   * libraries referenced by the project.xml file.  This 
   * does NOT include project dependencies.
   * @return
   */
  public List<I_Dependency> getNormalizedDependencies();
  
  /**
   * A list is used to keep order correlated with
   * the project.xml file.
   * @return
   */
  public List<I_ProjectDependency> getProjectDependencies();
  

  /**
   * Get the stage that came from the project.xml file,
   * with the specified name.
   * @return
   */
  public abstract I_RoutineBrief getStage(String name);

  /**
   * Get the stages that came from the project.xml file.
   * @return
   */
  public abstract Map<String, I_RoutineBrief> getStages();


  /**
   * Get the trait that came from the project.xml file,
   * with the specified name.
   * @return
   */
  public abstract I_RoutineBrief getTrait(String name);

  /**
   * Get the traits that came from the project.xml file.
   * @return
   */
  public abstract Map<String, I_RoutineBrief> getTraits();
}
