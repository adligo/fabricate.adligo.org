package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FabricateMutant implements I_Fabricate {
  private String fabricateHome_;
  private String fabricateXmlRunDir_;
  private String fabricateProjectRunDir_;
  private String fabricateDevXmlDir_;
  private String javaHome_;
  private String fabricateRepository_;
  private JavaSettingsMutant javaSettings_;
  private List<I_Dependency> dependencies_ = new ArrayList<I_Dependency>();
  private List<String> remoteRepositories_ = new ArrayList<String>();
  
  public FabricateMutant() {
  }
  
  public FabricateMutant(FabricateType fab, I_FabricateXmlDiscovery xmlDisc) {
    javaSettings_ = new JavaSettingsMutant(fab.getJava());
    FabricateDependencies deps =  fab.getDependencies();
    if (deps != null) {
      List<String> remoteRepos = deps.getRemoteRepository();
      setRemoteRepositories(remoteRepos);
      List<DependencyType> depTypes = deps.getDependency();
      if (depTypes != null) {
        for (DependencyType dep: depTypes) {
          addDependency(dep);
        }
      }
    }
    fabricateXmlRunDir_ = xmlDisc.getFabricateXmlDir();
    fabricateDevXmlDir_ = xmlDisc.getDevXmlDir();
    fabricateProjectRunDir_ = xmlDisc.getProjectXmlDir();
  }

  public FabricateMutant(I_Fabricate other) {
    fabricateHome_ = other.getFabricateHome();
    fabricateRepository_ = other.getFabricateRepository();
    javaHome_ = other.getJavaHome();
    fabricateXmlRunDir_ = other.getFabricateXmlRunDir();
    fabricateDevXmlDir_ = other.getFabricateDevXmlDir();
    fabricateProjectRunDir_ = other.getFabricateProjectRunDir();
    
    
    I_JavaSettings otherJava = other.getJavaSettings();
    if (otherJava == null) {
      javaSettings_ = new JavaSettingsMutant((JavaType) null);
    } else {
      javaSettings_ = new JavaSettingsMutant(otherJava);
    }
    List<String> remoteRepos = other.getRemoteRepositories();
    setRemoteRepositories(remoteRepos);
    
    List<I_Dependency> deps = other.getDependencies();
    setDependencies(deps);
  }

  public void addDependency(DependencyType dep) {
    dependencies_.add(new DependencyMutant(dep));
  }

  public void addDependency(I_Dependency dep) {
    if (dep instanceof DependencyMutant) {
      dependencies_.add(dep);
    } else if (dep != null) {
      dependencies_.add(new DependencyMutant(dep));
    }
  }
  
  public void addRemoteRepository(String repo) {
    if (repo != null) {
      remoteRepositories_.add(repo);
    }
  }
  
  public List<I_Dependency> getDependencies() {
    return dependencies_;
  }
  
  public String getFabricateDevXmlDir() {
    return fabricateDevXmlDir_;
  }

  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_Fabricate#getFabricateHome()
   */
  @Override
  public String getFabricateHome() {
    return fabricateHome_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_Fabricate#getFabricateRepository()
   */
  @Override
  public String getFabricateRepository() {
    return fabricateRepository_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_Fabricate#getJavaHome()
   */
  @Override
  public String getJavaHome() {
    return javaHome_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_Fabricate#getJavaSettings()
   */
  @Override
  public I_JavaSettings getJavaSettings() {
    return javaSettings_;
  }
  
  public List<String> getRemoteRepositories() {
    return remoteRepositories_;
  }
  
  public int getThreads() {
    if (javaSettings_ == null) {
      return FabricateDefaults.JAVA_THREADS;
    }
    return javaSettings_.getThreads();
  }

  public void setDependencies(Collection<? extends I_Dependency> deps) {
    dependencies_.clear();
    if (deps != null) {
      for(I_Dependency dep: deps) {
        addDependency(dep);
      }
    }
  }
  
  public void setFabricateHome(String fabricateHome) {
    this.fabricateHome_ = fabricateHome;
  }
  
  public void setFabricateRepository(String fabricateRepository) {
    this.fabricateRepository_ = fabricateRepository;
  }

  public void setFabricateXmlRunDir(String fabricateXmlRunDir) {
    this.fabricateXmlRunDir_ = fabricateXmlRunDir;
  }
  
  public void setJavaHome(String javaHome) {
    this.javaHome_ = javaHome;
  }

  
  public void setJavaSettings(I_JavaSettings javaSettings) {
    if (javaSettings instanceof JavaSettingsMutant) {
      javaSettings_ = (JavaSettingsMutant) javaSettings;
    } else {
      javaSettings_ = new JavaSettingsMutant(javaSettings);
    }
  }

  public void setRemoteRepositories(List<String> remoteRepositories) {
    remoteRepositories_.clear();
    if (remoteRepositories != null) {
      for (String repo: remoteRepositories) {
        addRemoteRepository(repo);
      }
    }
  }

  public String getFabricateXmlRunDir() {
    return fabricateXmlRunDir_;
  }



  public String getFabricateProjectRunDir() {
    return fabricateProjectRunDir_;
  }


  public void setFabricateProjectRunDir(String fabricateProjectRunDir) {
    this.fabricateProjectRunDir_ = fabricateProjectRunDir;
  }

  public void setFabricateDevXmlDir(String fabricateDevXmlDir) {
    this.fabricateDevXmlDir_ = fabricateDevXmlDir;
  }
  
}
