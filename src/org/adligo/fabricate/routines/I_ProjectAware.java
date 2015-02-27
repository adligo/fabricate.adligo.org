package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.project.I_Project;

public interface I_ProjectAware {
  public I_Project getProject();
  public void setProject(I_Project project);
}
