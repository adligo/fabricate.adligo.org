package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.depot.ArtifactMutant;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.routines.I_PlatformAware;

public class DepositJarTask extends ProjectAwareRoutine implements I_PlatformAware, I_JarFileNameAware {

  private I_Depot depot_;
  private String platform_;
  private String jarFileName_;
  
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    
    ensureDepotDirs();
    return super.setupInitial(memory, routineMemory);
  }


  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    
    super.setup(memory, routineMemory);
  }
  
  @Override
  public void run() {
    super.setRunning();
    if (log_.isLogEnabled(DepositJarTask.class)) {
      log_.println(DepositJarTask.class.getSimpleName() + " running with " + jarFileName_ );
    }
    CommonBuildDir cbd = new CommonBuildDir(files_);
    String buildDir = cbd.getBuildDir(project_);
    String wholeJar = buildDir + files_.getNameSeparator() + jarFileName_;
    
    ArtifactMutant am = new ArtifactMutant();
    am.setFileName(jarFileName_);
    am.setPlatformName(platform_);
    am.setType("jar");
    am.setProjectName(project_.getName());
    if (log_.isLogEnabled(DepositJarTask.class)) {
      log_.println(DepositJarTask.class.getSimpleName() + " moving " + wholeJar + " indo depot");
    }
    depot_.add(wholeJar, am);
  }

  public String getJarFileName() {
    return jarFileName_;
  }
  
  public String getPlatform() {
    return platform_;
  }
  
  public void setPlatform(String platform) {
    platform_ = platform;
  }
  
  public void setJarFileName(String jarFileName) {
    this.jarFileName_ = jarFileName;
  }

  private void ensureDepotDirs() {
    String depotDir = depot_.getDir();
    if (log_.isLogEnabled(DepositJarTask.class)) {
      log_.println("depot is in directory " + depotDir);
    }
    if (!files_.exists(depotDir)) {
      makeDir(depotDir);
    }
    
    String baseThingsDirName = "jars";
    if (platform_ != null) {
      if (!"jse".equalsIgnoreCase(platform_)) {
        baseThingsDirName = platform_.toLowerCase() + "_jars";
      }
    }
    String fullDir = depotDir + files_.getNameSeparator() + baseThingsDirName;
    if (!files_.exists(fullDir)) {
      makeDir(fullDir);
    }
  }
}
