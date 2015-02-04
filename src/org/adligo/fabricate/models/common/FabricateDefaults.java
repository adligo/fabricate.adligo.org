package org.adligo.fabricate.models.common;

import java.io.File;

public class FabricateDefaults {
  public static final FabricateDefaults INSTANCE = new FabricateDefaults();
  public static final String JAVA_XMX_DEFAULT = "64m";
  public static final String JAVA_XMS_DEFAULT = "16m";
  public static final int JAVA_THREADS = Runtime.getRuntime().availableProcessors() * 2;
  
  public static final String LOCAL_REPOSITORY = System.getProperty("user.home") + 
      File.separator + "local_repository";
  
  private FabricateDefaults() {}
}
