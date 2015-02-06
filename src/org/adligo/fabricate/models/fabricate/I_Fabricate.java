package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.List;

public interface I_Fabricate {

  public List<I_Dependency> getDependencies();
  
  public String getFabricateHome();

  public String getFabricateRepository();
  
  public String getJavaHome();

  public I_JavaSettings getJavaSettings();
  
  public List<String> getRemoteRepositories();
  
  public int getThreads();

}