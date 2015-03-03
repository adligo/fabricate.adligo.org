package org.adligo.fabricate.models.common;

/**
 * Represents a environment (i.e. command line environment variables)
 * shared by Fabricate for Processes it kicks off (i.e. git clone/ssh-agent)
 * @author scott
 *
 */
public interface I_ExecutionEnvironmentMutant extends I_ExecutionEnvironment,
I_FabricationMemoryMutant<String> {

}
