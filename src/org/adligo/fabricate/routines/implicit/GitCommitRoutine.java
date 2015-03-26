package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_AttributesContainer;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_ProjectAware;

/**
 * This routine runs a git commit on a project.
 * @author scott
 *
 */
public class GitCommitRoutine extends AbstractRoutine implements I_ProjectAware {
  public static final String NAME = "gitCommit";
  
  @Override
  public I_AttributesContainer getProject() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setProject(I_Project project) {
    // TODO Auto-generated method stub
    
  }

}
