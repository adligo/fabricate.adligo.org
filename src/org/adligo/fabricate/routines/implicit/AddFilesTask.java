package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.files.I_FileMatcher;
import org.adligo.fabricate.common.files.IncludesExcludesFileMatcher;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.routines.I_PlatformAware;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AddFilesTask extends ProjectAwareRoutine implements I_PlatformAware {
  private String platform_;
  
  @Override
  public String getPlatform() {
    return platform_;
  }

  @Override
  public void setPlatform(String platform) {
    platform_ = platform;
  }
  
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    return super.setup(memory, routineMemory);
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    super.setup(memory, routineMemory);
  }
  
  @Override
  public void run() {
    CommonBuildDir cbd = new CommonBuildDir(files_);
    
    String destDir = cbd.getClassesDir(project_, platform_);
    String includes = "includes";
    if (!"jse".equalsIgnoreCase(platform_)) {
      includes = platform_.toLowerCase() + " includes"; 
    }
    List<I_Parameter> includesList =  project_.getAttributes(includes);
    List<I_FileMatcher> includeMatchers = new ArrayList<I_FileMatcher>();
    for (I_Parameter inP: includesList) {
      includeMatchers.add(new PatternFileMatcher(files_, system_, inP.getValue(), true));
    }
    
    String excludes = "excludes";
    if (!"jse".equalsIgnoreCase(platform_)) {
      excludes = platform_.toLowerCase() + " excludes"; 
    }
    List<I_Parameter> excludesList =  project_.getAttributes(excludes);
    List<I_FileMatcher> excludesMatchers = new ArrayList<I_FileMatcher>();
    for (I_Parameter inP: excludesList) {
      excludesMatchers.add(new PatternFileMatcher(files_, system_, inP.getValue(), true));
    }
    
    //there must be a includes matcher to add something
    if (includeMatchers.size() >= 1) {
      I_FileMatcher matcher = new IncludesExcludesFileMatcher(includeMatchers, excludesMatchers);
      try {
        String projectDir = project_.getDir();
        List<String> files = files_.list(project_.getDir(), matcher);

        for (String file: files) {
          String relFile = file.substring(projectDir.length(), file.length());
          String to = destDir + files_.getNameSeparator() + relFile;
          files_.copy(file, to, StandardCopyOption.REPLACE_EXISTING);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
