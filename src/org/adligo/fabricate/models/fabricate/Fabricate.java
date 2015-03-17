package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.models.project.ProjectBrief;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * TODO add facets
 * TODO throw DuplicateRoutineExceptions
 * @author scott
 *
 */
public class Fabricate implements I_Fabricate {
  private final boolean developmentMode;
  private final List<I_Dependency> dependencies_;
  private final Map<String, I_RoutineBrief> commands_; 
  private final String fabricateHome_;
  private final String fabricateDevXmlDir_;
  
  private final String fabricateRepository_;
  private final String fabricateProjectRunDir_;
  private final String fabricateXmlRunDir_;
  private final Map<String, I_RoutineBrief> facets_; 
  
  private final String javaHome_;
  private final JavaSettings javaSettings_;
  
  private final String projectsDir_;
  private final List<I_ProjectBrief> projects_;
  private final List<String> remoteRepositories_;
  
  private final I_RoutineBrief scm_;
  private final Map<String, I_RoutineBrief> stages_; 
  private final Map<String, I_RoutineBrief> traits_; 
  
  public Fabricate(I_Fabricate other) {
    developmentMode = other.isDevelopmentMode();
    commands_ = createImmutableMap(other.getCommands());
    fabricateHome_ = other.getFabricateHome();
    fabricateXmlRunDir_ = other.getFabricateXmlRunDir();
    fabricateRepository_ = other.getFabricateRepository();
    facets_ = createImmutableMap(other.getFacets());
    
    javaHome_ = other.getJavaHome();
    I_JavaSettings otherJs = other.getJavaSettings();
    if (otherJs != null) {
      javaSettings_ = new JavaSettings(otherJs);
    } else {
      javaSettings_ = null;
    }
    fabricateDevXmlDir_ = other.getFabricateDevXmlDir();
    fabricateProjectRunDir_ = other.getFabricateProjectRunDir();
    
    projectsDir_ = other.getProjectsDir();
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
    
    List<I_ProjectBrief> projects = other.getProjects();
    if (projects != null) {
      List<I_ProjectBrief> newList = new ArrayList<I_ProjectBrief>();
      for (I_ProjectBrief proj: projects) {
        if (proj instanceof ProjectBrief) {
          newList.add(proj); 
        } else if (proj != null) {
          newList.add(new ProjectBrief(proj)); 
        }
      }
      projects_ = Collections.unmodifiableList(new ArrayList<I_ProjectBrief>(newList));
    } else {
      projects_ = Collections.emptyList();
    }
    
    I_RoutineBrief scm = other.getScm();
    if (scm != null) {
      scm_ = new RoutineBrief(scm);
    } else {
      scm_ = null;
    }
    stages_ = createImmutableMap(other.getStages());
    traits_ = createImmutableMap(other.getTraits());
  }

  @Override
  public I_RoutineBrief getCommand(String name) {
    return commands_.get(name);
  }
  
  @Override
  public Map<String, I_RoutineBrief> getCommands() {
    return commands_;
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


  public String getFabricateXmlRunDir() {
    return fabricateXmlRunDir_;
  }
  
  @Override
  public I_RoutineBrief getFacet(String name) {
    return facets_.get(name);
  }

  @Override
  public Map<String, I_RoutineBrief> getFacets() {
    return facets_;
  }
  
  private Map<String,I_RoutineBrief> createImmutableMap(Map<String,I_RoutineBrief> in) {
    if (in == null || in.size() == 0) {
      return Collections.emptyMap();
    }
    Map<String,I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    Set<Entry<String, I_RoutineBrief>> entries = in.entrySet();
    for (Entry<String, I_RoutineBrief> e: entries) {
      I_RoutineBrief v = e.getValue();
      if (v instanceof RoutineBrief) {
        toRet.put(e.getKey(), v);
      } else {
        //this will throw a npe for v == null
        toRet.put(e.getKey(), new RoutineBrief(v));
      }
    }
    return Collections.unmodifiableMap(toRet);
  }

  public String getProjectsDir() {
    return projectsDir_;
  }

  public boolean isDevelopmentMode() {
    return developmentMode;
  }

  @Override
  public List<String> getRemoteRepositories() {
    return remoteRepositories_;
  }
  
  @Override
  public I_RoutineBrief getStage(String name) {
    return stages_.get(name);
  }
  
  @Override
  public Map<String, I_RoutineBrief> getStages() {
    return stages_;
  }

  @Override
  public I_RoutineBrief getTrait(String name) {
    return traits_.get(name);
  }
  
  @Override
  public Map<String, I_RoutineBrief> getTraits() {
    return traits_;
  }
  
  public int getThreads() {
    if (javaSettings_ == null) {
      return FabricateDefaults.JAVA_THREADS;
    }
    return javaSettings_.getThreads();
  }

  @Override
  public List<I_ProjectBrief> getProjects() {
    return projects_;
  }

  @Override
  public I_RoutineBrief getScm() {
    return scm_;
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
}
