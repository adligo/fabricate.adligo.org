package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.AttributesOverlay;
import org.adligo.fabricate.models.common.I_AttributesContainer;
import org.adligo.fabricate.models.common.I_AttributesOverlay;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_ProjectAware;

public class ProjectAwareRoutine  extends FabricateAwareRoutine
implements I_ProjectAware, I_FabricateAware {
  
  protected I_Project project_;
  private I_AttributesOverlay overlay_;
  
  @Override
  public I_Project getProject() {
    return project_;
  }
  
  @Override
  public void setProject(I_Project project) {
    project_ = project;
  }
  
  public I_AttributesOverlay getOverlay() {
    if (overlay_ == null) {
      overlay_ = new AttributesOverlay(fabricate_, project_);
    }
    return overlay_;
  }
}
