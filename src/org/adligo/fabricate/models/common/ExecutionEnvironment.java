package org.adligo.fabricate.models.common;

import java.util.Map;

/**
 * Represents a environment (i.e. command line environment variables)
 * shared by Fabricate for Processes it kicks off (i.e. git clone/ssh-agent)
 * @author scott
 *
 */
public class ExecutionEnvironment extends FabricationMemory<String> implements 
  I_ExecutionEnvironment {

  public ExecutionEnvironment(I_ExecutionEnvironment env) {
    super(env);
  }

  @Override
  public void addAllTo(Map<String, String> env) {
    env.putAll(super.get());
  }
}
