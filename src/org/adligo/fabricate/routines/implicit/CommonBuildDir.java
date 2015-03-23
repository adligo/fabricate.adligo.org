package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.models.project.I_Project;

public class CommonBuildDir {
  private I_FabFileIO files_;
  
  public CommonBuildDir(I_FabFileIO files) {
    files_ = files;
  }
  
  public String getBuildDir(I_Project project) {
    return project.getDir() + "build";
  }
  
  public String getClassesDir(I_Project project, String platform) {
    String destDir = getBuildDir(project) + files_.getNameSeparator() + "classes";
    if (!"jse".equalsIgnoreCase(platform)) {
      destDir = getBuildDir(project) + files_.getNameSeparator() + platform.toLowerCase() +  "_classes";
    }
    return destDir;
  }
  
}
