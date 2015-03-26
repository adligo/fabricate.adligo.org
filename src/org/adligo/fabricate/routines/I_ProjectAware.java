package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_AttributesContainer;
import org.adligo.fabricate.models.project.I_Project;

public interface I_ProjectAware {
  public I_AttributesContainer getProject();
  public void setProject(I_Project project);
}
