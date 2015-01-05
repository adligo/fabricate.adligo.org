package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabContextMutant implements I_FabContext {
  public I_ProjectContext getProjectContext() {
    return projectContext_;
  }



  public void setProjectContext(I_ProjectContext projectContext) {
    this.projectContext_ = projectContext;
  }

  private FabRunType runType;
  private Map<String,String> args_ = new HashMap<String,String>();
  private String fabricateXmlPath;
  private String fabricateDirPath;
  private String projectPath;
  private String projectsPath;
  private String projectGroupsPath;
  private I_Depot depot;
  private String initialPath;
  private String outputPath;
  private Map<Class<?>,Boolean> logSettings = new HashMap<Class<?>,Boolean>();
  private FabricateType fabricate_;
  private FabricateProjectType project_;
  private String localRepositoryPath_;
  private String javaHome_;
  private String javaVersion_;
  private String fabricateVersion_;
  private I_ProjectContext projectContext_;
  private ConcurrentHashMap<String, Object> memory_ = new ConcurrentHashMap<String, Object>();
  
  public FabContextMutant() {
  }


  
  @Override
  public FabRunType getRunType() {
    return runType;
  }

  @Override
  public boolean hasArg(String key) {
    return args_.containsKey(key);
  }

  @Override
  public String getArgValue(String key) {
    return args_.get(key);
  }

  @Override
  public String getFabricateXmlPath() {
    return fabricateXmlPath;
  }

  @Override
  public String getFabricateDirPath() {
    return fabricateDirPath;
  }

  @Override
  public String getProjectPath() {
    return projectPath;
  }

  @Override
  public String getProjectsPath() {
    return projectsPath;
  }

  @Override
  public String getProjectGroupsPath() {
    return projectGroupsPath;
  }

  @Override
  public I_Depot getDepot() {
    return depot;
  }

  @Override
  public String getInitialPath() {
    return initialPath;
  }

  @Override
  public String getOutputPath() {
    return outputPath;
  }

  @SuppressWarnings("boxing")
  @Override
  public boolean isLogEnabled(Class<?> clazz) {
    Boolean toRet = logSettings.get(clazz);
    if (toRet != null) {
      return toRet;
    }
    return false;
  }
  
  @SuppressWarnings("boxing")
  public void setLogSetting(Class<?> clazz, boolean b) {
    logSettings.put(clazz, b);
  }

  public void setRunType(FabRunType runType) {
    this.runType = runType;
  }

  public void setFabricateXmlPath(String fabricateXmlPath) {
    this.fabricateXmlPath = fabricateXmlPath;
  }

  public void setFabricateDirPath(String fabricateDirPath) {
    this.fabricateDirPath = fabricateDirPath;
  }

  public void setProjectPath(String projectPath) {
    this.projectPath = projectPath;
  }

  public void setProjectsPath(String projectsPath) {
    this.projectsPath = projectsPath;
  }

  public void setProjectGroupsPath(String projectGroupsPath) {
    this.projectGroupsPath = projectGroupsPath;
  }

  public void setDepot(I_Depot depot) {
    this.depot = depot;
  }

  public void setInitialPath(String initialPath) {
    this.initialPath = initialPath;
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }
  
  @SuppressWarnings("boxing")
  public void checkDefaultLog(Class<?> c, boolean setting) {
    if (!logSettings.containsKey(c)) {
      logSettings.put(c, setting);
    }
  }

  public FabricateType getFabricate() {
    return fabricate_;
  }

  public void setFabricate(FabricateType fabricate) {
    fabricate_ = fabricate;
  }

  public FabricateProjectType getProject() {
    return project_;
  }

  public void setProject(FabricateProjectType project) {
    project_ = project;
  }

  public void setArgs(Map<String, String> args) {
    args_ = args;
  }

  public String getLocalRepositoryPath() {
    return localRepositoryPath_;
  }

  public void setLocalRepositoryPath(String localRepositoryPath) {
    localRepositoryPath_ = localRepositoryPath;
  }

  public String getJavaHome() {
    return javaHome_;
  }

  public void setJavaHome(String javaHome) {
    javaHome_ = javaHome;
  }

  public String getJavaVersion() {
    return javaVersion_;
  }

  public void setJavaVersion(String javaVersion) {
    javaVersion_ = javaVersion;
  }

  public String getFabricateVersion() {
    return fabricateVersion_;
  }

  public void setFabricateVersion(String fabricateVersion) {
    fabricateVersion_ = fabricateVersion;
  }

  @Override
  public void putInMemory(String key, Object value) {
    memory_.put(key, value);
  }

  @Override
  public Object getFromMemory(String key) {
    return memory_.get(key);
  }
}
