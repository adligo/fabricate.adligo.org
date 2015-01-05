package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

/**
 * This class helps the MavenObtainer determine
 * unique dependencies using equals and hashCode
 * @author scott
 *
 */
public class DependencyTypeHelper {
  private DependencyType dep_;
  
  public DependencyTypeHelper(DependencyType dep) {
    dep_ = dep;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    String artifact = dep_.getArtifact();
    result = prime * result + ((artifact == null) ? 0 : artifact.hashCode());
    String group = dep_.getGroup();
    result = prime * result + ((group == null) ? 0 : group.hashCode());
    String version = dep_.getVersion();
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    String type = dep_.getType();
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    DependencyTypeHelper other = (DependencyTypeHelper) obj;
    String artifact = dep_.getArtifact();
    if (artifact == null) {
      if (other.getArtifact() != null) {
        return false;
      }
    } else {
      if (!artifact.equals(other.getArtifact())) {
        return false;
      }
    }
    String group = dep_.getGroup();
    if (group == null) {
      if (other.getGroup() != null) {
        return false;
      }
    } else {
      if (!group.equals(other.getGroup())) {
        return false;
      }
    }
    String version = dep_.getVersion();
    if (version == null) {
      if (other.getVersion() != null) {
        return false;
      }
    } else {
      if (!version.equals(other.getVersion())) {
        return false;
      }
    }
    String type = dep_.getType();
    if (type == null) {
      if (other.getType() != null) {
        return false;
      }
    } else {
      if (!type.equals(other.getType())) {
        return false;
      }
    }
    return true;
  }

  public String getGroup() {
    return dep_.getGroup();
  }

  public String getArtifact() {
    return dep_.getArtifact();
  }

  public String getVersion() {
    return dep_.getVersion();
  }

  public String getType() {
    return dep_.getType();
  }
  
  public DependencyType getDependencyType() {
    return dep_;
  }
}
