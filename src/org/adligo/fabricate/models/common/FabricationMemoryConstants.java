package org.adligo.fabricate.models.common;

/**
 * This class contains the keys to fabrication shared memory 
 * between all routines.
 * @author scott
 *
 */
public class FabricationMemoryConstants {
  public static final FabricationMemoryConstants INSTANCE = new FabricationMemoryConstants();
  public static final String GIT_KEYSTORE_PASSWORD = "gitKeystorePassword";
  
  private FabricationMemoryConstants() {}
}
