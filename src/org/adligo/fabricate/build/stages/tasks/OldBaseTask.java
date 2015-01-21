package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class OldBaseTask implements I_FabTask {
  protected String projectsPath_;
  protected I_RunContext ctx_;
  protected I_FabLog log_;
  protected I_FabricateConstants constants_;
  protected NamedProject project_;
  protected String projectName_;
  protected Map<String, String> params_;
  protected Exception lastException_;
  protected String projectPath_;

  @Override
  public void setup(I_RunContext ctx, NamedProject project, Map<String, String> params) {
    ctx_ = ctx;
    log_ = ctx.getLog();
    constants_ = ctx.getConstants();
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
