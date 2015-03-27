package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.files.I_FileMatcher;
import org.adligo.fabricate.common.files.IncludesExcludesFileMatcher;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_AttributesOverlay;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.routines.I_PlatformAware;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    return super.setupInitial(memory, routineMemory);
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
    String includes = attribConstants_.getInclude();
    if (!"jse".equalsIgnoreCase(platform_)) {
      includes = platform_.toLowerCase() + " " + includes; 
    }
    I_AttributesOverlay overlay = getOverlay();
    List<I_Parameter> includesList =  overlay.getAllAttributes(includes);
    
    Set<String> dirs = new HashSet<String>();
    for(I_Parameter in: includesList) {
      String val = in.getValue();
      if (val != null) {
        dirs.add(val);
      }
    }
    String excludes = attribConstants_.getExclude();
    if (!"jse".equalsIgnoreCase(platform_)) {
      excludes = platform_.toLowerCase() + " " + excludes; 
    }
    
    for (String dir: dirs) {
      includesList =  overlay.getAllAttributes(includes, dir);
      List<I_Parameter> excludesList =  overlay.getAllAttributes(excludes, dir);
      
      List<I_FileMatcher> includeMatchers = new ArrayList<I_FileMatcher>();
      for (I_Parameter inP: includesList) {
        List<I_Parameter> files = inP.getChildren();
        for (I_Parameter file: files) {
          String value = file.getValue();
          if (value != null) {
            includeMatchers.add(new PatternFileMatcher(files_, system_, value, true));
          }
        }
      }
      
      List<I_FileMatcher> excludesMatchers = new ArrayList<I_FileMatcher>();
      for (I_Parameter inP: excludesList) {
        List<I_Parameter> files = inP.getChildren();
        for (I_Parameter file: files) {
          String value = file.getValue();
          if (value != null) {
            excludesMatchers.add(new PatternFileMatcher(files_, system_, inP.getValue(), true));
          }
        }
      }
      
      //there must be a includes matcher to add something
      if (includeMatchers.size() >= 1) {
        I_FileMatcher matcher = new IncludesExcludesFileMatcher(includeMatchers, excludesMatchers);
        try {
          String filterDir = project_.getDir() + files_.getNameSeparator() + dir;
          List<String> files = files_.list(filterDir, matcher);
  
          for (String file: files) {
            
            String relFile = file.substring(filterDir.length(), file.length());
            String to = destDir + files_.getNameSeparator() + relFile;
            File outFile = files_.instance(to);
            File outDir = outFile.getParentFile();
            String outDirPath = outDir.getAbsolutePath();
            if (!files_.exists(outDirPath)) {
              files_.mkdirs(outDirPath);
            }
            files_.copy(file, to, StandardCopyOption.REPLACE_EXISTING);
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
