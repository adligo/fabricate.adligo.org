package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.LogSettingType;
import org.adligo.fabricate.xml.io.LogSettingsType;
import org.adligo.fabricate.xml.io.ProjectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabContextMutant implements I_FabContext {
  private FabRunType runType;
  private Map<String,String> args;
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
  private ProjectType project_;
  
  public FabContextMutant() {
  }


  
  @Override
  public FabRunType getRunType() {
    return runType;
  }

  @Override
  public boolean hasArg(String key) {
    return args.containsKey(key);
  }

  @Override
  public String getArgValue(String key) {
    return args.get(key);
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



  public void setFabricate_(FabricateType fabricate) {
    fabricate_ = fabricate;
  }



  public ProjectType getProject_() {
    return project_;
  }



  public void setProject_(ProjectType project) {
    project_ = project;
  }
}
