package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.models.common.AttributesOverlay;
import org.adligo.fabricate.models.common.DuplicateRoutineException;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.models.project.ProjectBrief;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectsType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StageType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StagesAndProjectsType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StagesType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author scott
 *
 */
public class FabricateMutant implements I_Fabricate {
  /**
   * actually only ParameterMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always ParameterMutant.
   */
  private List<I_Parameter> attributes_ = new ArrayList<I_Parameter>();
  
  /**
   * actually only RoutineBriefMutant instances
   */
  private Map<String, I_RoutineBrief> commands_ = new HashMap<String, I_RoutineBrief>();
  
  private List<I_Dependency> dependencies_ = new ArrayList<I_Dependency>();
  private boolean developmentMode_ = false;
  /**
   * actually only RoutineBriefMutant instances
   */
  private Map<String, I_RoutineBrief> facets_ = new HashMap<String, I_RoutineBrief>();
  
  private String fabricateDevXmlDir_;
  private String fabricateHome_;
  
  /**
   * The directory where fabricate was run from
   * if it was a project directory (with project.xml)
   * and not a project group directory (with fabricate.xml).
   */
  private String fabricateProjectRunDir_;
  private String fabricateVersion_;
  private String fabricateXmlRunDir_;
  
  private String javaHome_;
  private String fabricateRepository_;
  private JavaSettingsMutant javaSettings_;
  
  private List<ProjectBrief> projects_ = new ArrayList<ProjectBrief>();
  private List<String> remoteRepositories_ = new ArrayList<String>();
  /**
   * actually only RoutineBriefMutant instances
   */
  private Map<String,I_RoutineBrief> traits_ = new HashMap<String,I_RoutineBrief>();
  private RoutineBriefMutant scm_;
  /**
   * actually only RoutineBriefMutant instances
   */
  private Map<String,I_RoutineBrief> stages_ = new HashMap<String,I_RoutineBrief>();
  private List<String> stageOrder_ = new ArrayList<String>();
  
  private String projectsDir_;
  
  public FabricateMutant() {
  }
  
  /**
   * Note this constructor does NOT create commands, stages, tasks  and traits
   * in order to save on script processing time.  The methods addCommands,
   * addStages and addTraits may be used to add the commands, stages and tasks.
   * 
   * @param fab
   * @param xmlDisc
   */
  public FabricateMutant(FabricateType fab, I_FabricateXmlDiscovery xmlDisc) throws ClassNotFoundException {
    ParamsType params = fab.getAttributes();
    if (params != null) {
      List<I_Parameter> converted = ParameterMutant.convert(params);
      attributes_.addAll(converted);
    }
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
    List<I_Parameter> attributes = other.getAttributes();
    if (attributes != null) {
      setAttributes(attributes);
    }
    developmentMode_ = other.isDevelopmentMode();
    fabricateHome_ = other.getFabricateHome();
    fabricateRepository_ = other.getFabricateRepository();
    javaHome_ = other.getJavaHome();
    
    fabricateDevXmlDir_ = other.getFabricateDevXmlDir();
    fabricateProjectRunDir_ = other.getFabricateProjectRunDir();
    
    fabricateVersion_ = other.getFabricateVersion();
    fabricateXmlRunDir_ = other.getFabricateXmlRunDir();
    
    I_JavaSettings otherJava = other.getJavaSettings();
    if (otherJava == null) {
      javaSettings_ = new JavaSettingsMutant((JavaType) null);
    } else {
      javaSettings_ = new JavaSettingsMutant(otherJava);
    }
    List<String> remoteRepos = other.getRemoteRepositories();
    setRemoteRepositories(remoteRepos);
    
    setCommands(other.getCommands());
    List<I_Dependency> deps = other.getDependencies();
    setDependencies(deps);
    setFacets(other.getFacets());
    
    setStages(other.getStages());
    setTraits(other.getTraits());
    projectsDir_ = other.getProjectsDir();
  }
  
  public void addAttribute(I_Parameter parameter) {
    ParameterMutant.addOrClone(parameter, attributes_);
  }
  
  public void addCommands(FabricateType type) 
      throws ClassNotFoundException {
    List<RoutineParentType> routines =  type.getCommand();
    addRoutineParents(routines, commands_, RoutineBriefOrigin.FABRICATE_COMMAND);
  }

  public void addDependency(DependencyType dep) {
    dependencies_.add(new DependencyMutant(dep, null));
  }

  public void addDependency(I_Dependency dep) {
    if (dep instanceof DependencyMutant) {
      dependencies_.add(dep);
    } else if (dep != null) {
      dependencies_.add(new DependencyMutant(dep));
    }
  }
  
  public void addFacets(Collection<RoutineParentType> routines) 
      throws ClassNotFoundException {
    addRoutineParents(routines, facets_, RoutineBriefOrigin.FABRICATE_FACET);
  }
  
  public void addRemoteRepository(String repo) {
    if (repo != null) {
      remoteRepositories_.add(repo);
    }
  }
  
  public void addStage(I_RoutineBrief rb) {
    String name = rb.getName();
    if (stageOrder_.contains(name)) {
      throw new IllegalArgumentException("Duplicate stage name " + name);
    }
    stageOrder_.add(name);
    if (rb instanceof RoutineBriefMutant) {
      stages_.put(name, rb);
    } else {
      stages_.put(name, new RoutineBriefMutant(rb));
    }
  }
  
  public void addStages(FabricateType type) throws ClassNotFoundException {
    StagesAndProjectsType spt =  type.getProjectGroup();
    if (spt != null) {
      StagesType stages = spt.getStages();
      if (stages != null) {
        List<StageType> stageTypes = stages.getStage();
        
        if (stageTypes != null) {
          for (StageType routine: stageTypes) {
            RoutineBriefMutant mut = new RoutineBriefMutant(routine, RoutineBriefOrigin.FABRICATE_STAGE);
            addStage(mut);
          }
        }
        
        List<StageType> archiveTypes = stages.getArchiveStage();
        
        if (archiveTypes != null) {
          for (StageType routine: archiveTypes) {
            RoutineBriefMutant mut = new RoutineBriefMutant(routine, RoutineBriefOrigin.FABRICATE_ARCHIVE_STAGE);
            stages_.put(mut.getName(), mut);
          }
        }
      }
      
      if (spt != null) {
        ProjectsType projects =  spt.getProjects();
        if (projects != null) {
          RoutineType scm = projects.getScm();
          if (scm != null) {
            scm_ = new RoutineBriefMutant(scm, RoutineBriefOrigin.FABRICATE_SCM);
          }
          List<ProjectType> projectsList =  projects.getProject();
          for (ProjectType proj: projectsList) {
            if (proj != null) {
              projects_.add(new ProjectBrief(proj));
            }
          }
        }
      }
    }
  }


  
  public void addScmAndProjects(FabricateType type) throws ClassNotFoundException {
    StagesAndProjectsType spt =  type.getProjectGroup();
    if (spt != null) {
      
      
      if (spt != null) {
        ProjectsType projects =  spt.getProjects();
        if (projects != null) {
          RoutineType scm = projects.getScm();
          if (scm != null) {
            scm_ = new RoutineBriefMutant(scm, RoutineBriefOrigin.FABRICATE_SCM);
          }
          List<ProjectType> projectsList =  projects.getProject();
          for (ProjectType proj: projectsList) {
            if (proj != null) {
              projects_.add(new ProjectBrief(proj));
            }
          }
        }
      }
    }
  }
  
  public void addTraits(Collection<RoutineParentType> routines) 
      throws ClassNotFoundException {
    addRoutineParents(routines, traits_, RoutineBriefOrigin.FABRICATE_TRAIT);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_AttributesContainer#getAttributes()
   */
  @Override
  public List<I_Parameter> getAttributes() {
    //protect from external modification
    return new ArrayList<I_Parameter>(attributes_);
  }

  /* (non-Javadoc)
  * @see org.adligo.fabricate.models.common.I_AttributesContainer#getAttribute(String key)
  */
 @Override
  public I_Parameter getAttribute(String key) {
    return AttributesOverlay.getAttribute(key, attributes_);
  }
  
 /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_AttributesContainer#getAttributes(String key)
   */
  @Override
  public List<I_Parameter> getAttributes(String key) {
    return AttributesOverlay.getAttributes(key, attributes_);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_AttributesContainer#getAttributes(String key, String value)
   */
  @Override
  public List<I_Parameter> getAttributes(String key, String value) {
    return AttributesOverlay.getAttributes(key, value, attributes_);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_AttributesContainer#getAttributeValue(String key)
   */
  @Override
  public String getAttributeValue(String key) {
    return AttributesOverlay.getAttributeValue(key, attributes_);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.common.I_AttributesContainer#getAttributeValues(String key)
   */
  @Override
  public List<String> getAttributeValues(String key) {
    return AttributesOverlay.getAttributeValues(key, attributes_);
  }
  
  @Override
  public I_RoutineBrief getCommand(String name) {
    return commands_.get(name);
  }
  
  public Map<String, I_RoutineBrief> getCommands() {
    return new HashMap<String,I_RoutineBrief>(commands_);
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

  public String getFabricateProjectRunDir() {
    return fabricateProjectRunDir_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_Fabricate#getFabricateRepository()
   */
  @Override
  public String getFabricateRepository() {
    return fabricateRepository_;
  }
  
  public String getFabricateVersion() {
    return fabricateVersion_;
  }
  
  public String getFabricateXmlRunDir() {
    return fabricateXmlRunDir_;
  }

  public I_RoutineBrief getFacet(String name) {
    return facets_.get(name);
  }
  
  public Map<String,I_RoutineBrief> getFacets() {
    return new HashMap<String,I_RoutineBrief>(facets_);
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
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_Fabricate#getProjects()
   */
  public List<I_ProjectBrief> getProjects() {
    return new ArrayList<I_ProjectBrief>(projects_);
  }
  

  public String getProjectsDir() {
    return projectsDir_;
  }
  
  public List<String> getRemoteRepositories() {
    return remoteRepositories_;
  }
  
  public I_RoutineBrief getScm() {
    return scm_;
  }

  @Override
  public I_RoutineBrief getStage(String name) {
    return stages_.get(name);
  }
  
  public Map<String, I_RoutineBrief> getStages() {
    return new HashMap<String,I_RoutineBrief>(stages_);
  }

  public List<String> getStageOrder() {
    return new ArrayList<String>(stageOrder_);
  }
  
  @Override
  public I_RoutineBrief getTrait(String name) {
    return traits_.get(name);
  }
  
  public Map<String, I_RoutineBrief> getTraits() {
    return new HashMap<String,I_RoutineBrief>(traits_);
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
  
  public boolean isDevelopmentMode() {
    return developmentMode_;
  }

  public void setCommands(Map<String, I_RoutineBrief> commands) {
    commands_.clear();
    if (commands != null && commands.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> entries = commands.entrySet();
      for (Entry<String,I_RoutineBrief> e: entries) {
        I_RoutineBrief v = e.getValue();
        if (v instanceof RoutineBriefMutant) {
          commands_.put(e.getKey(), (RoutineBriefMutant) v);
        } else {
          commands_.put(e.getKey(), new RoutineBriefMutant(v));
        }
      }
    }
  }
  
  public void setAttributes(Collection<? extends I_Parameter> attributes) {
    ParameterMutant.setMutants(attributes_, attributes);
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

  public void setFabricateVersion(String fabricateVersion) {
    this.fabricateVersion_ = fabricateVersion;
  }
  
  public void setFabricateXmlRunDir(String fabricateXmlRunDir) {
    this.fabricateXmlRunDir_ = fabricateXmlRunDir;
  }
  
  public void setFacets(Map<String, I_RoutineBrief> facets) {
    facets_.clear();
    if (facets != null && facets.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> entries = facets.entrySet();
      for (Entry<String,I_RoutineBrief> e: entries) {
        I_RoutineBrief v = e.getValue();
        if (v instanceof RoutineBriefMutant) {
          facets_.put(e.getKey(), (RoutineBriefMutant) v);
        } else {
          facets_.put(e.getKey(), new RoutineBriefMutant(v));
        }
      }
    }
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


  public void setFabricateProjectRunDir(String fabricateProjectRunDir) {
    this.fabricateProjectRunDir_ = fabricateProjectRunDir;
  }

  public void setFabricateDevXmlDir(String fabricateDevXmlDir) {
    this.fabricateDevXmlDir_ = fabricateDevXmlDir;
  }

  public void setStages(Map<String, I_RoutineBrief> stages) {
    stages_.clear();
    if (stages != null && stages.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> entries = stages.entrySet();
      for (Entry<String,I_RoutineBrief> e: entries) {
        I_RoutineBrief v = e.getValue();
        addStage(v);
      }
    }
  }
  
  public void setTraits(Map<String, I_RoutineBrief> traits) {
    traits_.clear();
    if (traits != null && traits.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> entries = traits.entrySet();
      for (Entry<String,I_RoutineBrief> e: entries) {
        I_RoutineBrief v = e.getValue();
        if (v instanceof RoutineBriefMutant) {
          traits_.put(e.getKey(), (RoutineBriefMutant) v);
        } else {
          //should throw a npe for null v
          traits_.put(e.getKey(), new RoutineBriefMutant(v));
        }
      }
    }
  }

  public void setProjectsDir(String projectsDir) {
    projectsDir_ = projectsDir;
  }

  public void setDevelopmentMode(boolean developmentMode) {
    this.developmentMode_ = developmentMode;
  }

  public void setProjects(List<? extends I_ProjectBrief> projects) {
    projects_.clear();
    for (I_ProjectBrief proj: projects) {
      if (proj instanceof ProjectBrief) {
        projects_.add((ProjectBrief) proj);
      } else if (proj != null) {
        projects_.add(new ProjectBrief(proj));
      }
    }
  }

  public void setScm(RoutineBriefMutant scm) {
    this.scm_ = scm;
  }

  private void addRoutineParents(Collection<RoutineParentType> routines, 
      Map<String,I_RoutineBrief> map, RoutineBriefOrigin origin) 
          throws IllegalArgumentException, ClassNotFoundException {
    if (routines != null) {
      for (RoutineParentType routine: routines) {
        RoutineBriefMutant mut = new RoutineBriefMutant(routine, origin);
        String name = mut.getName();
        if (map.containsKey(name)) {
          DuplicateRoutineException dre = new DuplicateRoutineException();
          dre.setName(name);
          dre.setOrigin(mut.getOrigin());
          throw dre;
        } else {
          map.put(mut.getName(), mut);
        }
      }
    }
  }

}
