package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.NamedProject;

import java.util.Map;

public abstract class BaseTask implements I_FabTask {
  protected String projectsPath_;
  protected I_FabContext ctx_;
  protected NamedProject project_;
  protected String projectName_;
  protected Map<String, String> params_;
  protected Exception lastException_;
  
  @Override
  public void setup(I_FabContext ctx, NamedProject project, Map<String, String> params) {
    ctx_ = ctx;
    project_ = project;
    projectName_ = project.getName();
    projectsPath_ = ctx_.getProjectsPath();
    params_ = params;
  }
  
  public Exception getLastSetupException() {
    return lastException_;
  }
  
  public boolean hadSetupException () {
    if (lastException_ == null) {
      return false;
    }
    return true;
  }
  
}
