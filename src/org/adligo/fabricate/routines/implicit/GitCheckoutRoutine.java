package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_ProjectBriefAware;

import java.util.List;

/**
 * This class does a git clone for a project.
 * 
 * @author scott
 *
 */
public class GitCheckoutRoutine extends AbstractRoutine 
  implements I_ProjectBriefAware, I_InputAware<ScmContext> {
  public static final String NAME = "gitClone";
  
  @Override
  public I_ProjectBrief getProjectBrief() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setProjectBrief(I_ProjectBrief brief) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setInput(ScmContext input) {
    // TODO Auto-generated method stub
    
  }

}
