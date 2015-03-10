package org.adligo.fabricate.models.project;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectType;

/**
 * A immutable ProjectBrief.
 * 
 * @author scott
 *
 */
public class ProjectBrief implements I_ProjectBrief {
  private final String name_;
  private final String shortName_;
  private final String domanName_;
  private final String version_;
  
  public ProjectBrief(I_ProjectBrief project) {
    this(project.getName(), project.getVersion());
  }
  
  public ProjectBrief(ProjectType project) {
    this(project.getName(), project.getVersion());
  }

  public ProjectBrief(String name, String version) {
    name_ = name;
    int idx = name_.indexOf(".");
    if (idx != -1) {
      shortName_ = name_.substring(0, idx);
      domanName_ = name_.substring(idx + 1, name_.length());
    } else {
      shortName_ = name;
      domanName_ = "";
    }
    if (StringUtils.isEmpty(version)) {
      version_ = "";
    } else {
      version_ = version;
    }
  }
  
  @Override
  public String getName() {
    return name_;
  }

  @Override
  public String getVersion() {
    return version_;
  }

  @Override
  public String getShortName() {
    return shortName_;
  }

  @Override
  public String getDomainName() {
    return domanName_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
    result = prime * result + ((version_ == null) ? 0 : version_.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    ProjectBrief other = (ProjectBrief) obj;
    if (name_ == null) {
      if (other.name_ != null)
        return false;
    } else if (!name_.equals(other.name_))
      return false;
    if (version_ == null) {
      if (other.version_ != null)
        return false;
    } else if (!version_.equals(other.version_))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "ProjectBrief [name=" + name_ + ", version=" + version_ + "]";
  }
}
