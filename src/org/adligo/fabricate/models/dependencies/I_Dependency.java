package org.adligo.fabricate.models.dependencies;

import java.util.List;

/**
 * Represents a dependency which has been read
 * from the xml file.  Implementation may be immutable
 * or mutable.  Equals and hashCode are done performing the following fields;
 * Artifact
 * FileName
 * Group
 * Platform
 * Type
 * Version
 * @author scott
 *
 */
public interface I_Dependency {
  /**
   * Information about IDE (Integrated Development Environment [i.e. Eclipse]) specific settings for this dependency.  
   * @return
   */
  public I_Ide get(int child);
  
  public abstract String getArtifact();

  /**
   * Information about IDE (Integrated Development Environment [i.e. Eclipse]) specific settings for this dependency.  
   * @return
   */
  public abstract List<I_Ide> getChildren();

  public abstract boolean isExtract();

  public abstract String getFileName();

  public abstract String getGroup();

  public abstract String getPlatform();

  public abstract String getType();

  public abstract String getVersion();

  public int size();
}