package org.adligo.fabricate.models.project;

import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectType;

/**
 * A immu
 * @author scott
 *
 */
public class ProjectBrief implements I_ProjectBrief {
  private String name_;
  private String version_;
  
  public ProjectBrief(ProjectType project) {
    name_ = project.getName();
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
}
