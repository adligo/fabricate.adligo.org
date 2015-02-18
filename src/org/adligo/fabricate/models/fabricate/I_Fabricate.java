package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.List;
import java.util.Map;

public interface I_Fabricate extends I_JavaSettings {

  public List<I_Dependency> getDependencies();
  
  public Map<String, I_RoutineBrief> getCommands();
  
  public String getFabricateHome();

  public String getFabricateProjectRunDir();

  public String getFabricateDevXmlDir();
  
  public String getFabricateXmlRunDir();
  
  public String getFabricateRepository();
  
  public String getJavaHome();

  public I_JavaSettings getJavaSettings();
  
  public List<String> getRemoteRepositories();
  
  public Map<String, I_RoutineBrief> getStages();
  
  public Map<String, I_RoutineBrief> getTraits();

}