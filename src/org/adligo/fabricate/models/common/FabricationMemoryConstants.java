package org.adligo.fabricate.models.common;

import java.util.Collections;
import java.util.Map;

/**
 * This class contains the keys to fabrication shared memory 
 * between all routines.
 * @author scott
 *
 */
public class FabricationMemoryConstants {
  public static final FabricationMemoryConstants INSTANCE = new FabricationMemoryConstants();
  
  /**
   * key to a {@link I_ExecutionEnvironment(Mutant)}, which represents
   * the environment (shell environment variables) to pass to 
   * child processes (initially designed so git doesn't try to 
   * do it's own ssh agent sub processes which seems to be
   * impossible to control through java).
   */
  public static final String ENV = "environment";
  public static final I_ExecutionEnvironment EMPTY_ENV = new ExecutionEnvironment(new ExecutionEnvironmentMutant());
  
  /**
   * key to a String
   */
  public static final String GIT_KEYSTORE_PASSWORD = "gitKeystorePassword";
  /**
   * key to a ConcurrentLinkedQueue<String> of the projects
   * that were cloned.
   */
  public static final String CLONED_PROJECTS = "gitClonedProjects";
  
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
