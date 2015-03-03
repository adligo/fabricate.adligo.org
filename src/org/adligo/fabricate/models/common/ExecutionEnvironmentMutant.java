package org.adligo.fabricate.models.common;

import java.util.Map;

/**
 * Represents a environment (i.e. command line environment variables)
 * shared by Fabricate for Processes it kicks off (i.e. git clone/ssh-agent)
 * @author scott
 *
 */
public class ExecutionEnvironmentMutant extends FabricationMemoryMutant<String>
  implements I_ExecutionEnvironmentMutant {

  @Override
  public void addAllTo(Map<String, String> env) {
    env.putAll(super.get());
  }

}
