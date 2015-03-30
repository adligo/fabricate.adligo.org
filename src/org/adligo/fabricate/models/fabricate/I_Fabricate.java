package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.models.common.I_AttributesContainer;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.project.I_ProjectBrief;

import java.util.List;
import java.util.Map;

/**
 * TODO add facets
 * @author scott
 *
 */
public interface I_Fabricate extends I_JavaSettings, I_AttributesContainer {

  public I_RoutineBrief getArchiveStage(String name);
  public Map<String, I_RoutineBrief> getArchiveStages();
  public List<String> getArchiveStageOrder();
  
  public List<I_Dependency> getDependencies();
  
  public I_RoutineBrief getCommand(String name);
  public Map<String, I_RoutineBrief> getCommands();
  
  public String getFabricateHome();

  public String getFabricateProjectRunDir();

  public String getFabricateDevXmlDir();
  
  public String getFabricateVersion();
  public String getFabricateXmlRunDir();
  
  public String getFabricateRepository();
  
  public I_RoutineBrief getFacet(String name);
  public Map<String,I_RoutineBrief> getFacets();
  
  public String getJavaHome();

  public I_JavaSettings getJavaSettings();
  
  public List<I_ProjectBrief> getProjects();
  /**
   * includes the last File.separatorChar.
   * @return
   */
  public String getProjectsDir();
  
  public List<String> getRemoteRepositories();
  
  public I_RoutineBrief getScm();
  
  public I_RoutineBrief getStage(String name);
  public List<String> getStageOrder();
  public Map<String, I_RoutineBrief> getStages();
  
  public I_RoutineBrief getTrait(String name);
  public Map<String, I_RoutineBrief> getTraits();
  
  public boolean isDevelopmentMode();

}