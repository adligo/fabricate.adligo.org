package org.adligo.fabricate.models.project;

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
  
  public ProjectBrief(ProjectType project) {
    name_ = project.getName();
    int idx = name_.indexOf(".");
    if (idx != -1) {
      shortName_ = name_.substring(0, idx);
      domanName_ = name_.substring(idx + 1, name_.length());
    } else {
      shortName_ = null;
      domanName_ = null;
    }
    version_ = project.getVersion();
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
}
