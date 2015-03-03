package org.adligo.fabricate.models.common;

import java.util.Map;

/**
 * Represents a environment (i.e. command line environment variables)
 * shared by Fabricate for Processes it kicks off (i.e. git clone/ssh-agent)
 * @author scott
 *
 */
public interface I_ExecutionEnvironment extends I_FabricationMemory<String> {
  /**
   * Adds all of the current key,value 
   * pairs to the env parameter.
   * @param env
   */
  public void addAllTo(Map<String,String> env);
}
