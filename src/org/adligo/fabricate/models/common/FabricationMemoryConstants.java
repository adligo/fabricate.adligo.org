package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.en.SystemEnMessages;

/**
 * This class contains the keys to fabrication shared memory 
 * between all routines.
 * @author scott
 *
 */
public class FabricationMemoryConstants {
  
  /**
   * key to a ConcurrentLinkedQueue&lt;String&gt; of the projects
   * that were cloned.
   */
  public static final String CLONED_PROJECTS = "gitClonedProjects";
  
  /**
   * key to a I_Depot which can be used to share
   * files between project builds.
   */
  public static final String DEPOT = "depot";
  
  /**
   * Key to a unmodifiable List&lt;Dependency&gt; usually from the 'load projects' facet.
   */
  public static final String DEPENDENCIES = "dependencies";

  /**
   * Key to a String usually from FabricateController.
   */
  public static final String JAVA_HOME = "javaHome";
  
  /**
   * Key to a JavaFactory usually from FabricateController.
   */
  public static final String JAVA_FACTORY = "javaFactory";
  
  /**
   * key to a {@link I_ExecutionEnvironment(Mutant)}, which represents
   * the environment (shell environment variables) to pass to 
   * child processes (initially designed so git doesn't try to 
   * start it's own ssh agent sub processes which seems to be
   * impossible to control through java).
   */
  public static final String ENV = "environment";
  public static final I_ExecutionEnvironment EMPTY_ENV = new ExecutionEnvironment(
      new ExecutionEnvironmentMutant(SystemEnMessages.INSTANCE));

  public static final FabricationMemoryConstants INSTANCE = new FabricationMemoryConstants();
  
  /**
   * Key to a unmodifiable List<Project> usually from the 'load projects' facet.
   */
  public static final String LOADED_PROJECTS = "loadedProjects";
  /**
   * Key to a unmodifiable Map<String,Project> usually from the 'load projects' facet.
   */
  public static final String LOADED_PROJECTS_MAP = "loadedProjectsMap";
  
  /**
   * Key to a unmodifiable List<Project> usually from the 'setup projects' facet.
   */
  public static final String PARTICIPATING_PROJECTS = "participatingProjects";
  
  /**
   * Key to a unmodifiable List<String> of lower case platforms which were entered
   * on the command line.  If nothing was entered on the command line this will contain
   * 'jse' usually from the 'setup projects' facet.
   */
  public static final String PLATFORMS = "platforms";
  
  /**
   * key to a boolean which shows that
   * the ssh-agent (ssh-add) has bee setup,
   * so that the ENV contains the correct information
   * for git clone, git pull so that a new dialog
   * is not created on each thread.
   */
  public static final String SETUP_SSH_AGENT = "setupSshAgent";
  
  private FabricationMemoryConstants() {}
}
