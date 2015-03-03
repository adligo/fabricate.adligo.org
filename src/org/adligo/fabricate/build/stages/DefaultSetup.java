package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.tasks.OldCompileTask;
import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.I_FabSetupStage;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.RunContextMutant;
import org.adligo.fabricate.common.files.FabFileIO;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.common.system.GitCalls;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.depot.Depot;
import org.adligo.fabricate.depot.DepotManager;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.java.JavaJar;
import org.adligo.fabricate.repository.RepositoryManager;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.LogSettingType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.LogSettingsType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSetup 
/*
implements I_FabSetupStage 
*/
{
  /*
  private final I_FabSystem sys_;
  private final I_FabFileIO files_; 
  private final I_FabXmlFileIO xmlFiles_; 
  private I_FabLog log_;
  private String initalDir_;
  private FabricateType fabricate_;
  private FabricateProjectType project_;
  private String fabricateXmlPath_;
  private String projectXmlPath_;
  private DepotManager depotManager_;
  private I_Depot depot_;
  
  public DefaultSetup() {
    sys_ = new FabSystem();
    files_ = sys_.getFileIO(); 
    xmlFiles_ = sys_.getXmlFileIO(); 
  }
  
  @Override
  public void setInitalDirPath(String initalDir) {
    initalDir_ = initalDir;
  }

  @Override
  public void setFabricate(FabricateType fabricate) {
    fabricate_ = fabricate;
  }

  @Override
  public void setProject(FabricateProjectType project) {
    project_ = project;
  }

  @Override
  public I_RunContext setup(Map<String, String> args) {
    RunContextMutant fcm = new RunContextMutant();
    setupLogging(fcm);
    fcm.setArgs(args);
    fcm.setFabricate(fabricate_);
    fcm.setProject(project_);
    fcm.setFabricateVersion(args.get("fabricate_version"));
    fcm.setInitialPath(initalDir_);
    fcm.setFabricateXmlPath(fabricateXmlPath_);
    fcm.setJavaHome(System.getenv("JAVA_HOME"));
    fcm.setJavaVersion(args.get("java"));
    fcm.setFabricateDirPath(fabricateXmlPath_.substring(0, fabricateXmlPath_.length() - 14));
    if (projectXmlPath_ != null) {
      fcm.setProjectPath(projectXmlPath_.substring(0, projectXmlPath_.length() - 12));
    }
    
    String fabricateDir = fcm.getFabricateDirPath();
    String depot = null;
    if (args.containsKey("depot")) {
      depot = args.get("depot");
    } else {
      depot = fabricateDir + File.separator + "depot";
    }
    depotManager_ = new DepotManager(sys_);
    depotManager_.setup(depot);
    depot_ = depotManager_.getDepot();
    fcm.setDepot(depot_);
    
    cleanDir(fcm, fabricateDir, "output");
    fcm.setOutputPath(fabricateDir + File.separator + "output");
    fcm.setLocalRepositoryPath(FabricateDefaults.LOCAL_REPOSITORY);
    
    
    if (project_ != null) {
      log_.println("running in project mode");
      fcm.setRunType(FabRunType.PROJECT);
      fcm.setProjectPath(initalDir_);
    } else {
      depotManager_.clean();
      
      if (args.containsKey("dev")) {
        log_.println("running in developer mode");
        fcm.setRunType(FabRunType.DEVELOPMENT);
        File fabDir = new File(fabricateDir);
        File file = fabDir.getParentFile();
        fcm.setProjectsPath(file.getAbsolutePath());
        createDevFile(file.getAbsolutePath() + File.separator + "dev.xml",fabDir.getName());
      } else {
        //must be the default setting
        log_.println("running in default mode");
        fcm.setRunType(FabRunType.DEFAULT);
        cleanDir(fcm, fabricateDir,"projects");
        fcm.setProjectsPath(fabricateDir + File.separator + "projects");
      }
    }
    return fcm;
  }

  private void createDevFile(String file, String content) {
    FabricateDevType fdt = new FabricateDevType();
    fdt.setProjectGroup(content);
    try {
      xmlFiles_.writeDev_v1_0(file, fdt);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void cleanDir(RunContextMutant fcm, String fabricateDir, String dir) {
    String fullDir = fabricateDir + File.separator + dir;
    File dirFile = new File(fullDir);
    if (dirFile.exists()) {
      try {
        if (log_.isLogEnabled(DefaultSetup.class)) {
          log_.println("Cleaning " + fullDir);
        }
        files_.deleteRecursive(fullDir);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (!dirFile.mkdir()) {
      throw new RuntimeException("There was a problem creating the " + dir + " directory " + 
          System.lineSeparator() + fullDir);
    }
  }

  public String getFabricateXmlPath() {
    return fabricateXmlPath_;
  }

  public void setFabricateXmlPath(String fabricateXmlPath) {
    fabricateXmlPath_ = fabricateXmlPath;
  }

  public String getProjectXmlPath() {
    return projectXmlPath_;
  }

  public void setProjectXmlPath(String projectXmlPath) {
    projectXmlPath_ = projectXmlPath;
  }

  @SuppressWarnings("boxing")
  public void setupLogging(RunContextMutant fcm) {
    LogSettingsType logs = fabricate_.getLogs();
    Map<String,Boolean> settings = new HashMap<String,Boolean>();
    if (logs != null) {
      List<LogSettingType> logTypes = logs.getLog();
      for (LogSettingType setting: logTypes) {
        String clazz = setting.getClazz();
        Boolean b = setting.isSetting();
        if (b == null) {
          b = false;
        }
        try {
          Class<?> c = Class.forName(clazz);
          settings.put(c.getName(), b);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    }
    //alpha ordered default ons
    checkDefaultLog(OldBaseConcurrentStage.class, true, settings);
    checkDefaultLog(OldCompileTask.class, true, settings);
    checkDefaultLog(CompileJarAndDeposit.class, true, settings);
    checkDefaultLog(DefaultSetup.class, true, settings);
    checkDefaultLog(DepotManager.class, true, settings);
    checkDefaultLog(FabFileIO.class, true, settings);
    checkDefaultLog(GitStage.class, true, settings);
    checkDefaultLog(JavaJar.class, true, settings);
    checkDefaultLog(LoadAndCleanProjects.class, true, settings);
    checkDefaultLog(OldCompileTask.class, true, settings);
    
    checkDefaultLog(Depot.class, true, settings);
    
  //alpha ordered default offs
    checkDefaultLog(GitCalls.class, false, settings);
    checkDefaultLog(RepositoryManager.class, false, settings);
    //JavaJar true can cause the jar process to hang?
    log_ = sys_.getLog();
    fcm.setLog(log_);
  }
  
  private void checkDefaultLog(Class<?> clazz, boolean value, Map<String,Boolean> settings) {
    String className = clazz.getName();
    if (settings.get(className) == null) {
      settings.put(className, value);
    }
  }
  */
}
