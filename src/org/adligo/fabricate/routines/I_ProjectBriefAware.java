package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.project.I_ProjectBrief;

public interface I_ProjectBriefAware {
  public I_ProjectBrief getProjectBrief();
  public void setProjectBrief(I_ProjectBrief brief);
}
