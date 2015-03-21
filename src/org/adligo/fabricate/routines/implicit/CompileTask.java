package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependencyType;

import java.io.File;
import java.util.List;

public class CompileTask extends ProjectAwareRoutine {
  private static final String SRC_DIRS = "srcDirs";
  private I_RepositoryPathBuilder repositoryPathBuilder_;
  
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    
    return super.setup(memory, routineMemory);
  }


  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    // TODO Auto-generated method stub
    super.setup(memory, routineMemory);
  }
  
  @Override
  public void run() {
    repositoryPathBuilder_ = new DefaultRepositoryPathBuilder(
        fabricate_.getFabricateRepository(), files_.getNameSeparator());
    String [] srcDirs = getSrcDirs();
    String buildDir = project_.getDir() + files_.getNameSeparator() + "build";
    makeDir(buildDir);
    
    String destDir = buildDir + files_.getNameSeparator() + "classes";
    makeDir(destDir);
    
    super.run();
  }

  private String [] getSrcDirs() {
    I_Parameter firstSrcDirs = brief_.getParameter(SRC_DIRS);
    String [] srcDirs = firstSrcDirs.getValueDelimited(",");
    if (srcDirs.length == 1) {
      if (StringUtils.isEmpty(srcDirs[0])) {
        srcDirs[0] = "src";
      }
    } 
    for (int i = 0; i < srcDirs.length; i++) {
      String dir = srcDirs[i];
      srcDirs[i] = project_.getDir() + files_.getNameSeparator() + dir;
    }
    return srcDirs;
  }

  
  private void buildClasspath(StringBuilder sb) {

    List<I_Dependency> deps = project_.getNormalizedDependencies();
    if (deps != null) {
      for (I_Dependency dep: deps) {
        String jarFilePath = repositoryPathBuilder_.getArtifactPath(new Dependency(dep));
        if (sb.length() >= 1) {
          sb.append(system_.getPathSeparator());
        }
        if (!files_.exists(jarFilePath)) {
          String message = sysMessages_.getTheFollowingRequiredFileIsMissing() +
              system_.lineSeparator() + jarFilePath;
          throw new RuntimeException(message);
        }
        sb.append(jarFilePath);
      }
    }
    /*
    List<I_ProjectDependency> projects = depsType.getProject();
    if (projects != null) {
      I_Depot depot = ctx_.getDepot();
      for (ProjectDependencyType project: projects) { 
        String projectName = project.getValue();
        String file = depot.get(projectName, "jar");
        if (sb.length() >= 1) {
          sb.append(File.pathSeparator);
        } 
        sb.append(file);
      }
    }
    */
  }
}
