package org.adligo.fabricate.depot;

public interface I_Artifact {

  public abstract String getType();

  public abstract String getPlatformName();
  
  public abstract String getProjectName();

  public abstract String getFileName();
}