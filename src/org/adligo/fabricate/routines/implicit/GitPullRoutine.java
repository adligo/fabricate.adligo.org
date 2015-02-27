package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_ProjectBriefAware;

/**
 * This routine does a git pull on a project.
 * @author scott
 *
 */
public class GitPullRoutine extends AbstractRoutine implements I_ProjectBriefAware {
  public static final String NAME = "gitPull";
  
  @Override
  public I_ProjectBrief getProjectBrief() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setProjectBrief(I_ProjectBrief brief) {
    // TODO Auto-generated method stub
    
  }

}
