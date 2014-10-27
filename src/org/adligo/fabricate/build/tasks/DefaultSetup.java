package org.adligo.fabricate.build.tasks;

import org.adligo.fabricate.build.run.TaskManager;
import org.adligo.fabricate.common.Depot;
import org.adligo.fabricate.common.FabContextMutant;
import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.FileUtils;
import org.adligo.fabricate.common.I_Depot;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabSetupTask;
import org.adligo.fabricate.external.GitCalls;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.LogSettingType;
import org.adligo.fabricate.xml.io.LogSettingsType;
import org.adligo.fabricate.xml.io.ProjectType;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class DefaultSetup implements I_FabSetupTask {
  private static PrintStream OUT = System.out;
  private String initalDir_;
  private FabricateType fabricate_;
  private ProjectType project_;
  private String fabricateXmlPath_;
  private String projectXmlPath_;
  private I_Depot depot_;
  
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
    FabContextMutant fcm = new FabContextMutant();
    setupLogging(fcm);
    fcm.setArgs(args);
    fcm.setFabricate(fabricate_);
    fcm.setProject(project_);
    fcm.setInitialPath(initalDir_);
    fcm.setFabricateXmlPath(fabricateXmlPath_);
    fcm.setFabricateDirPath(fabricateXmlPath_.substring(0, fabricateXmlPath_.length() - 14));
    if (projectXmlPath_ != null) {
      fcm.setProjectPath(projectXmlPath_.substring(0, projectXmlPath_.length() - 12));
    }
    
    String fabricateDir = fcm.getFabricateDirPath();
    String depot = null;
    if (args.containsKey("depot")) {
      depot = args.get("depot");
      depot_ = new Depot(depot, fcm);
      fcm.setDepot(depot_);
    } else {
      depot = fabricateDir + File.separator + "depot";
      depot_ = new Depot(depot, fcm);
      fcm.setDepot(new Depot(fabricateDir + File.separator + "depot",fcm));
    }
    cleanDir(fcm, fabricateDir, "output");
    fcm.setOutputPath(fabricateDir + File.separator + "output");
    
    if (project_ != null) {
      cleanForProjectRun(fcm);
    } else {
      cleanDepot(fcm, depot);
      
      if (args.containsKey("dev")) {
        File fabDir = new File(fabricateDir);
        File file = fabDir.getParentFile();
        fcm.setProjectsPath(file.getAbsolutePath());
      } else {
        //must be the default setting
        fcm.setRunType(FabRunType.DEFAULT);
        cleanDir(fcm, fabricateDir,"projects");
        fcm.setProjectsPath(fabricateDir + File.separator + "projects");
      }
    }
    return fcm;
  }

  
  public void cleanDir(FabContextMutant fcm, String fabricateDir, String dir) {
    String fullDir = fabricateDir + File.separator + dir;
    File dirFile = new File(fullDir);
    if (dirFile.exists()) {
      try {
        if (fcm.isLogEnabled(DefaultSetup.class)) {
          OUT.println("Cleaning " + fullDir);
        }
        FileUtils.removeRecursive(Paths.get(dirFile.toURI()), fcm);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (!dirFile.mkdir()) {
      throw new RuntimeException("There was a problem creating the " + dir + " directory " + 
          System.lineSeparator() + fullDir);
    }
  }

  public void cleanDepot(FabContextMutant fcm, String depot) {
    if (fcm.isLogEnabled(DefaultSetup.class)) {
      OUT.println("Cleaning " + depot);
    }
    depot_.clean();
  }

  public void cleanForProjectRun(FabContextMutant fcm) {
    fcm.setRunType(FabRunType.PROJECT);
    //initalDir_ must then be the project directory
    String projectPath = fcm.getProjectPath();
    String buildPath = projectPath + File.separator + "build";
    
    try {
      FileUtils.removeRecursive(
          Paths.get(new File(buildPath).toURI()), fcm);
    } catch (IOException e) {
      throw new RuntimeException(e);
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
  public void setupLogging(FabContextMutant fcm) {
    LogSettingsType logs = fabricate_.getLogs();
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
          fcm.setLogSetting(c, b);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    }
    fcm.checkDefaultLog(DefaultSetup.class, true);
    fcm.checkDefaultLog(FileUtils.class, true);
    fcm.checkDefaultLog(TaskManager.class, true);
    fcm.checkDefaultLog(GitCalls.class, true);
    fcm.checkDefaultLog(GitObtainer.class, true);
  }
  

}
