package org.adligo.fabricate.depot;

import org.adligo.fabricate.common.util.StringUtils;

public class ArtifactKey {
  private final String projectName_;
  private final String artifactType_;
  private final String platformName_;
  
  public ArtifactKey(String projectName, String artifactType, String platformName) {
    if (StringUtils.isEmpty(projectName)) {
      throw new IllegalArgumentException("projectName");
    }
    projectName_ = projectName;
    if (StringUtils.isEmpty(artifactType)) {
      throw new IllegalArgumentException("artifactType");
    }
    artifactType_ = artifactType;
    if (StringUtils.isEmpty(platformName)) {
      throw new IllegalArgumentException("platformName");
    }
    platformName_ = platformName;
  }
  
  public String getProjectName() {
    return projectName_;
  }
  
  public String getArtifactType() {
    return artifactType_;
  }
  
  public String getPlatformName() {
    return platformName_;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + artifactType_.hashCode();
    result = prime * result + platformName_.hashCode();
    result = prime * result + projectName_.hashCode();
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ArtifactKey other = (ArtifactKey) obj;
    if (!artifactType_.equals(other.artifactType_)) {
      return false;
    }
    if (!platformName_.equals(other.platformName_)) {
      return false;
    }
    if (!projectName_.equals(other.projectName_)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "ArtifactKey [projectName=" + projectName_ + ", artifactType=" + artifactType_
        + ", platformName=" + platformName_ + "]";
  }
  
}
