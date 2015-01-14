package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.common.log.I_FabLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class BaseTask implements I_FabTask {
  protected String projectsPath_;
  protected I_FabContext ctx_;
  protected I_FabLog log_;
  protected NamedProject project_;
  protected String projectName_;
  protected Map<String, String> params_;
  protected Exception lastException_;
  protected String projectPath_;

  @Override
  public void setup(I_FabContext ctx, NamedProject project, Map<String, String> params) {
    ctx_ = ctx;
    log_ = ctx.getLog();
    project_ = project;
    projectName_ = project.getName();
    projectsPath_ = ctx_.getProjectsPath();
    params_ = params;
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
  
  public String [] getDelimitedValue(String key, String delimitor, Map<String,String> params) {
    String srcDirsParams = params.get(key);
    String [] srcDirs = null;
    
    if (StringUtils.isEmpty(srcDirsParams)) {
      srcDirs = new String[] {"src"};
    } else {
      StringTokenizer st = new StringTokenizer(srcDirsParams, delimitor);
      List<String> tokens = new ArrayList<String>();
      while (st.hasMoreTokens()) {
        tokens.add(st.nextToken());
      }
      srcDirs = tokens.toArray(new String[tokens.size()]);
    }
    for (int i = 0; i < srcDirs.length; i++) {
      srcDirs[i] = projectsPath_ + File.separator + projectName_ + File.separator +
          srcDirs[i];
    }
    return srcDirs;
  }
}
