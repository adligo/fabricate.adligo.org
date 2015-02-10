package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fabricate implements I_Fabricate {
  private final List<I_Dependency> dependencies_;
  
  private final String fabricateHome_;
  private final String fabricateDevXmlDir_;
  
  private final String fabricateRepository_;
  private final String fabricateProjectRunDir_;
  private final String fabricateXmlRunDir_;
  
  private final String javaHome_;
  private final JavaSettings javaSettings_;
  
  private final List<String> remoteRepositories_;
  
  public Fabricate(I_Fabricate other) {
    fabricateHome_ = other.getFabricateHome();
    fabricateXmlRunDir_ = other.getFabricateXmlRunDir();
    fabricateRepository_ = other.getFabricateRepository();
    javaHome_ = other.getJavaHome();
    I_JavaSettings otherJs = other.getJavaSettings();
    if (otherJs != null) {
      javaSettings_ = new JavaSettings(otherJs);
    } else {
      javaSettings_ = null;
    }
    fabricateDevXmlDir_ = other.getFabricateDevXmlDir();
    fabricateProjectRunDir_ = other.getFabricateProjectRunDir();
    
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
  
  public String getFabricateDevXmlDir() {
    return fabricateDevXmlDir_;
  }
  
  public String getFabricateHome() {
    return fabricateHome_;
  }

  public String getFabricateProjectRunDir() {
    return fabricateProjectRunDir_;
  }
  
  public String getFabricateRepository() {
    return fabricateRepository_;
  }
  
  public String getJavaHome() {
    return javaHome_;
  }

  public I_JavaSettings getJavaSettings() {
    return javaSettings_;
  }

  @Override
  public List<String> getRemoteRepositories() {
    return remoteRepositories_;
  }
  
  public int getThreads() {
    if (javaSettings_ == null) {
      return FabricateDefaults.JAVA_THREADS;
    }
    return javaSettings_.getThreads();
  }

  public String getXms() {
    if (javaSettings_ == null) {
      return FabricateDefaults.JAVA_XMS_DEFAULT;
    }
    return javaSettings_.getXms();
  }

  public String getXmx() {
    if (javaSettings_ == null) {
      return FabricateDefaults.JAVA_XMX_DEFAULT;
    }
    return javaSettings_.getXmx();
  }
  public String getFabricateXmlRunDir() {
    return fabricateXmlRunDir_;
  }

  

}
