package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.run.DepotManager;
import org.adligo.fabricate.build.run.StageManager;
import org.adligo.fabricate.build.stages.tasks.CompileTask;
import org.adligo.fabricate.common.Depot;
import org.adligo.fabricate.common.FabContextMutant;
import org.adligo.fabricate.common.FabRunType;
import org.adligo.fabricate.common.I_Depot;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabSetupStage;
import org.adligo.fabricate.common.LocalRepositoryHelper;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.external.GitCalls;
import org.adligo.fabricate.external.JavaJar;
import org.adligo.fabricate.external.RepositoryDownloader;
import org.adligo.fabricate.external.files.FileUtils;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.LogSettingType;
import org.adligo.fabricate.xml.io.LogSettingsType;
import org.adligo.fabricate.xml.io.dev.FabricateDevType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;
import org.adligo.fabricate.xml_io.DevIO;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class DefaultSetup implements I_FabSetupStage {
  private String initalDir_;
  private FabricateType fabricate_;
  private FabricateProjectType project_;
  private String fabricateXmlPath_;
  private String projectXmlPath_;
  private DepotManager depotManager_;
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
  public void setProject(FabricateProjectType project) {
    project_ = project;
  }

  @Override
  public I_FabContext setup(Map<String, String> args) {
    FabContextMutant fcm = new FabContextMutant();
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
    depotManager_ = new DepotManager(fcm, depot);
    depot_ = depotManager_.getDepot();
    fcm.setDepot(depot_);
    
    cleanDir(fcm, fabricateDir, "output");
    fcm.setOutputPath(fabricateDir + File.separator + "output");
    LocalRepositoryHelper lrh = new LocalRepositoryHelper();
    String localRepository = lrh.getRepositoryPath(fabricate_);
    fcm.setLocalRepositoryPath(localRepository);
    
    
    if (project_ != null) {
      ThreadLocalPrintStream.println("running in project mode");
      fcm.setRunType(FabRunType.PROJECT);
      fcm.setProjectPath(initalDir_);
    } else {
      depotManager_.clean();
      
      if (args.containsKey("dev")) {
        ThreadLocalPrintStream.println("running in developer mode");
        fcm.setRunType(FabRunType.DEVELOPMENT);
        File fabDir = new File(fabricateDir);
        File file = fabDir.getParentFile();
        fcm.setProjectsPath(file.getAbsolutePath());
        createDevFile(file.getAbsolutePath() + File.separator + "dev.xml",fabDir.getName());
      } else {
        //must be the default setting
        ThreadLocalPrintStream.println("running in default mode");
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
      DevIO.write(fdt, file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void cleanDir(FabContextMutant fcm, String fabricateDir, String dir) {
    String fullDir = fabricateDir + File.separator + dir;
    File dirFile = new File(fullDir);
    if (dirFile.exists()) {
      try {
        if (fcm.isLogEnabled(DefaultSetup.class)) {
          ThreadLocalPrintStream.println("Cleaning " + fullDir);
        }
        FileUtils fus = new FileUtils(fcm);
        fus.removeRecursive(Paths.get(dirFile.toURI()));
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
    //alpha ordered default ons
    fcm.checkDefaultLog(BaseConcurrentStage.class, true);
    fcm.checkDefaultLog(CompileTask.class, true);
    fcm.checkDefaultLog(CompileJarAndDeposit.class, true);
    fcm.checkDefaultLog(DefaultSetup.class, true);
    fcm.checkDefaultLog(DepotManager.class, true);
    fcm.checkDefaultLog(FileUtils.class, true);
    fcm.checkDefaultLog(GitObtainer.class, true);
    fcm.checkDefaultLog(JavaJar.class, true);
    fcm.checkDefaultLog(LoadAndCleanProjects.class, true);
    fcm.checkDefaultLog(MavenDownloader.class, true);
    fcm.checkDefaultLog(StageManager.class, true);
    fcm.checkDefaultLog(CompileTask.class, true);
    
    fcm.checkDefaultLog(Depot.class, true);
    
  //alpha ordered default offs
    fcm.checkDefaultLog(GitCalls.class, false);
    fcm.checkDefaultLog(RepositoryDownloader.class, false);
    //JavaJar true can cause the jar process to hang?
    
  }
  

}
