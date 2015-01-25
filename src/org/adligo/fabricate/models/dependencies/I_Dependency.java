package org.adligo.fabricate.models.dependencies;

import java.util.List;

public interface I_Dependency {
  public I_Ide get(int child);
  
  public abstract String getArtifact();

  public abstract List<I_Ide> getChildren();

  public abstract boolean isExtract();

  public abstract String getFileName();

  public abstract String getGroup();

  public abstract String getPlatform();

  public abstract String getType();

  public abstract String getVersion();

  public int size();
}