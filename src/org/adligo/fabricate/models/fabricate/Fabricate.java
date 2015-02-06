package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fabricate implements I_Fabricate {
  private final List<I_Dependency> dependencies_;
  
  private final String fabricateHome;
  private final String fabricateRepository;
  
  private final String javaHome;
  private final JavaSettings javaSettings;
  
  private final List<String> remoteRepositories_;
  
  public Fabricate(I_Fabricate other) {
    fabricateHome = other.getFabricateHome();
    fabricateRepository = other.getFabricateRepository();
    javaHome = other.getJavaHome();
    javaSettings = new JavaSettings(other.getJavaSettings());
    
    List<String> remotRepos = other.getRemoteRepositories();
    if (remotRepos == null) {
      remoteRepositories_ = Collections.emptyList();
    } else {
      List<String> newList = new ArrayList<String>();
      for (String repo: remotRepos) {
        if (repo != null) {
          if (!newList.contains(repo)) {
            newList.add(repo);
          }
        }
      }
      remoteRepositories_ = Collections.unmodifiableList(newList);
    }
    
    List<I_Dependency> deps = other.getDependencies();
    if (deps != null) {
      List<I_Dependency> newList = new ArrayList<I_Dependency>();
      for (I_Dependency dep: deps) {
        if (dep instanceof Dependency) {
          newList.add(dep); 
        } else if (dep != null) {
          newList.add(new Dependency(dep)); 
        }
      }
      dependencies_ = Collections.unmodifiableList(new ArrayList<I_Dependency>(newList));
    } else {
      dependencies_ = Collections.emptyList();
    }
  }

  @Override
  public List<I_Dependency> getDependencies() {
    return dependencies_;
  }
  
  public String getFabricateHome() {
    return fabricateHome;
  }

  public String getFabricateRepository() {
    return fabricateRepository;
  }
  
  public String getJavaHome() {
    return javaHome;
  }

  public I_JavaSettings getJavaSettings() {
    return javaSettings;
  }

  @Override
  public List<String> getRemoteRepositories() {
    return remoteRepositories_;
  }
  
  @Override
  public int getThreads() {
    return javaSettings.getThreads();
  }

  

}
