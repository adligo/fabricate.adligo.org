package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.I_ParamsTree;
import org.adligo.fabricate.common.I_ProjectContext;
import org.adligo.fabricate.common.I_StageContext;
import org.adligo.fabricate.common.log.I_FabLog;

import java.io.File;

public abstract class BaseTask implements I_FabTask {
  protected String projectsPath_;
  protected I_FabContext ctx_;
  protected I_FabLog log_;
  protected I_ProjectContext project_;
  protected String projectName_;
  protected Exception lastException_;
  protected String projectPath_;

  @Override
  public void setup(I_FabContext ctx, I_StageContext stageContext, I_ProjectContext project) {
    ctx_ = ctx;
    log_ = ctx.getLog();
    project_ = project;
    projectName_ = project.getName();
    projectsPath_ = ctx_.getProjectsPath();
    projectPath_ = projectsPath_ + File.separator +  projectName_;
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

  public String getProjectPath() {
    return projectPath_;
  }
  
}
