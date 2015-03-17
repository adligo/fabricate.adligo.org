package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_ProjectBriefAware;

public class ProjectBriefAwareRoutine  extends FabricateAwareRoutine 
implements I_ProjectBriefAware, I_FabricateAware {
  
  protected I_ProjectBrief brief_;
  
  @Override
  public I_ProjectBrief getProjectBrief() {
    return brief_;
  }
  
  @Override
  public void setProjectBrief(I_ProjectBrief brief) {
    brief_ = brief;
  }
  
}
