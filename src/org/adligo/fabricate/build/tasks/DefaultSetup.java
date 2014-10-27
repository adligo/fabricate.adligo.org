package org.adligo.fabricate.build.tasks;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabSetupTask;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.ProjectType;

import java.util.Map;

public class DefaultSetup implements I_FabSetupTask {
  private String initalDir_;
  private FabricateType fabricate_;
  private ProjectType project_;
  
  @Override
  public void setInitalDirPath(String initalDir) {
    initalDir_ = initalDir;
  }

  @Override
  public void setFabricate(FabricateType fabricate) {
    fabricate_ = fabricate;
  }

  @Override
  public void setProject(ProjectType project) {
    project_ = project;
  }

  @Override
  public I_FabContext setup(Map<String, String> args) {
    
    if (project_ != null) {
      
    } else {
      
    }
    return null;
  }

  
  private void setupProject(String projectDir) {
    
  }
}
