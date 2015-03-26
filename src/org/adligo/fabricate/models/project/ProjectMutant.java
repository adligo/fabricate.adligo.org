package org.adligo.fabricate.models.project;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.AttributesOverlay;
import org.adligo.fabricate.models.common.DuplicateRoutineException;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_LibraryDependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.dependencies.LibraryDependencyMutant;
import org.adligo.fabricate.models.dependencies.ProjectDependencyMutant;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependenciesType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectRoutineType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectStagesType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMutant implements I_Project {
  public static boolean equals(Object o, I_Project p) {
    if (o == p) {
      return true;
    }
    if (o instanceof I_Project) {
      I_Project other = (I_Project) o;
      String name = p.getName();
      String oName = other.getName();
      if (name == null) {
        if (oName == null) {
          return true;
        }
        return false;
      } else if (name.equals(oName)) {
        return true;
      }
      return false;
    }
    return false;
  }
  
  public static int hashCode(I_Project p) {
    final int prime = 31;
    int result = 1;
    String name = p.getName();
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }
  
  public static String toString(I_Project p) {
    StringBuilder sb = new StringBuilder();
    sb.append(p.getClass().getSimpleName());
    sb.append(" [name=");
    sb.append(p.getName());
    sb.append(", dir=");
    sb.append(p.getDir());
    String version = p.getVersion();
    if (!StringUtils.isEmpty(version)) {
      sb.append(", version=");
      sb.append(version);
    }
    
    List<I_Parameter> attribs = p.getAttributes();
    if (attribs != null) {
      sb.append(", attributes=");
      sb.append(attribs.size());
    }
    
    Map<String,I_RoutineBrief> commands = p.getCommands();
    if (commands != null) {
      sb.append(", commands=");
      sb.append(commands.size());
    }
    
    List<I_Dependency> deps = p.getDependencies();
    if (deps != null) {
      sb.append(", dependencies=");
      sb.append(deps.size());
    }
    
    List<I_LibraryDependency> ldeps = p.getLibraryDependencies();
    if (ldeps != null) {
      sb.append(", libraryDependencies=");
      sb.append(ldeps.size());
    }
    
    List<I_Dependency> ndeps = p.getNormalizedDependencies();
    if (ndeps != null) {
      sb.append(", normalizedDependencies=");
      sb.append(ndeps.size());
    }
    
    List<I_ProjectDependency> pdeps = p.getProjectDependencies();
    if (pdeps != null) {
      sb.append(", projectDependencies=");
      sb.append(pdeps.size());
    }
    
    
    Map<String,I_RoutineBrief> stages = p.getStages();
    if (stages != null) {
      sb.append(", stages=");
      sb.append(stages.size());
    }
    
    Map<String,I_RoutineBrief> traits = p.getTraits();
    if (traits != null) {
      sb.append(", traits=");
      sb.append(traits.size());
    }
    sb.append("]");
    return sb.toString();
  }
  
  /**
   * actually only ParameterMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always ParameterMutant.
   */
  private List<I_Parameter> attributes_ = new ArrayList<I_Parameter>();
      
  /**
   * actually only RoutineBriefMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always RoutineBriefMutant.
   */
  private Map<String, I_RoutineBrief> commands_ =
      new HashMap<String, I_RoutineBrief>();
      
  /**
   * The absolute directory path
   */
  private String dir_;
  
  private String name_;
  private String version_;
  
  /**
   * actually only DependencyMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always DependencyMutant.
   */
  private List<I_Dependency> dependencies_ = 
      new ArrayList<I_Dependency>();
      
  /**
   * actually only ProjectDependencyMutant instances
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always ProjectDependencyMutant.
   */
  private List<I_LibraryDependency> libraryDependencies_ = 
          new ArrayList<I_LibraryDependency>();
          
  /**
   * actually only DependencyMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always DependencyMutant.
   */
  private List<I_Dependency> normalizedDependencies_ = 
      new ArrayList<I_Dependency>();
      
  /**
   * actually only ProjectDependencyMutant instances
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always ProjectDependencyMutant.
   */
  private List<I_ProjectDependency> projectDependencies_ = 
      new ArrayList<I_ProjectDependency>();
  
  /**
   * actually only RoutineBriefMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always RoutineBriefMutant.
   */
  private Map<String, I_RoutineBrief> stages_ =
      new HashMap<String, I_RoutineBrief>();
      
  /**
   * actually only RoutineBriefMutant instances.
   * This instance must be protected from external modification
   * outside of this class to ensure that the values are always RoutineBriefMutant.
   */
  private Map<String, I_RoutineBrief> traits_ =
      new HashMap<String, I_RoutineBrief>();
      
  public ProjectMutant() {}
  
  public ProjectMutant(I_Project project) {
    dir_ = project.getDir();
    name_ = project.getName();
    version_ = project.getVersion();
    setAttributes(project.getAttributes());
    Map<String, I_RoutineBrief> cmds = project.getCommands();
    if (cmds != null && cmds.size() >= 1) {
      setCommands(cmds.values());
    }
    setDependencies(project.getDependencies());
    setLibraryDependencies(project.getLibraryDependencies());
    setNormalizedDependencies(project.getNormalizedDependencies());
    setProjectDependencies(project.getProjectDependencies());
    Map<String, I_RoutineBrief> stages = project.getStages();
    if (stages != null && stages.size() >= 1) {
      setStages(stages.values());
    }
    Map<String, I_RoutineBrief> traits = project.getTraits();
    if (traits != null && traits.size() >= 1) {
      setTraits(traits.values());
    }
  }
  
  public ProjectMutant(String dir, I_ProjectBrief project, FabricateProjectType fabProject) throws ClassNotFoundException {
    dir_ = dir;
    name_ = project.getName();
    version_ = project.getVersion();
    
    ParamsType pt = fabProject.getAttributes();
    List<I_Parameter> pms = ParameterMutant.convert(pt);
    attributes_.addAll(pms);
    
    ProjectDependenciesType pdt = fabProject.getDependencies();
    if (pdt != null) {
      List<I_Dependency> deps =  DependencyMutant.convert(pdt.getDependency());
      setDependencies(deps);
      
      List<LibraryDependencyMutant> lDeps =  LibraryDependencyMutant.convert(pdt.getLibrary());
      setLibraryDependencies(lDeps);
      
      List<ProjectDependencyMutant> pDeps =  ProjectDependencyMutant.convert(pdt.getProject());
      setProjectDependencies(pDeps);  
    }
    
    List<ProjectRoutineType> cmds = fabProject.getCommand();
    if (cmds != null && cmds.size() >= 1) {
      addProjectRoutineParents(cmds, commands_, RoutineBriefOrigin.PROJECT_COMMAND);
    }

    ProjectStagesType pst = fabProject.getStages();
    if (pst != null) {
      List<ProjectRoutineType> stages = pst.getStage();
      if (stages != null && stages.size() >= 1) {
        addProjectRoutineParents(stages, stages_, RoutineBriefOrigin.PROJECT_STAGE);
      }
    }
    
    List<RoutineParentType> traits = fabProject.getTrait();
    if (traits != null && traits.size() >= 1) {
      addRoutineParents(traits, traits_, RoutineBriefOrigin.PROJECT_TRAIT);
    }
  }
  
  public void addAttribute(I_Parameter parameter) {
    ParameterMutant.addOrClone(parameter, attributes_);
  }
  
  public void addCommand(I_RoutineBrief cmd) {
    String name = cmd.getName();
    if (commands_.containsKey(name)) {
      DuplicateRoutineException dre = new DuplicateRoutineException();
      dre.setName(name);
      dre.setOrigin(cmd.getOrigin());
      dre.setParentName(name_);
      throw dre;
    } else {
      if (cmd instanceof RoutineBriefMutant) {
        commands_.put(name, cmd);
      } else {
        commands_.put(name, new RoutineBriefMutant(cmd));
      }
    }
  }
  
  public void addDependency(I_Dependency dep) {
    //dedupe
    if (!dependencies_.contains(dep)) {
      if (dep instanceof DependencyMutant) {
        dependencies_.add(dep);
      } else {
        dependencies_.add(new DependencyMutant(dep));
      }
    }
  }

  public void addLibraryDependency(I_LibraryDependency dep) {
    //dedupe
    if (!libraryDependencies_.contains(dep)) {
      if (dep instanceof LibraryDependencyMutant) {
        libraryDependencies_.add(dep);
      } else {
        libraryDependencies_.add(new LibraryDependencyMutant(dep));
      }
    }
  }
  
  public void addNormalizedDependency(I_Dependency dep) {
    //dedupe
    if (!normalizedDependencies_.contains(dep)) {
      if (dep instanceof DependencyMutant) {
        normalizedDependencies_.add(dep);
      } else {
        normalizedDependencies_.add(new DependencyMutant(dep));
      }
    }
  }
  
  public void addProjectDependency(I_ProjectDependency dep) {
    //dedupe
    if (!projectDependencies_.contains(dep)) {
      if (dep instanceof ProjectDependencyMutant) {
        projectDependencies_.add(dep);
      } else {
        projectDependencies_.add(new ProjectDependencyMutant(dep));
      }
    }
  }   
  
  public void addStage(I_RoutineBrief stage) {
    //dedupe
    String name = stage.getName();
    if (stages_.containsKey(name)) {
      DuplicateRoutineException dre = new DuplicateRoutineException();
      dre.setName(name);
      dre.setOrigin(stage.getOrigin());
      dre.setParentName(name_);
      throw dre;
    } else {
      if (stage instanceof RoutineBriefMutant) {
        stages_.put(name, stage);
      } else {
        stages_.put(name, new RoutineBriefMutant(stage));
      }
    }
  }
  
  public void addTrait(I_RoutineBrief trait) {
    //dedupe
    String name = trait.getName();
    if (traits_.containsKey(name)) {
      DuplicateRoutineException dre = new DuplicateRoutineException();
      dre.setName(name);
      dre.setOrigin(trait.getOrigin());
      dre.setParentName(name_);
      throw dre;
    } else {
      if (trait instanceof RoutineBriefMutant) {
        traits_.put(name, trait);
      } else {
        traits_.put(name, new RoutineBriefMutant(trait));
      }
    }
  }
  
  @Override
  public boolean equals(Object obj) {
    return equals(obj, this);
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
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_Project#getCommand(java.lang.String)
   */
  @Override
  public I_RoutineBrief getCommand(String name) {
    return commands_.get(name);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_Project#getCommands()
   */
  @Override
  public Map<String, I_RoutineBrief> getCommands() {
    //protect from external modification
    return new HashMap<String, I_RoutineBrief>(commands_);
  }
  
  public List<I_Dependency> getDependencies() {
    //protect from external modification
    return new ArrayList<I_Dependency>(dependencies_);
  }

  @Override
  public String getDomainName() {
    if (name_ != null) {
      return new ProjectBrief(name_, version_).getDomainName();
    }
    return null;
  }
  
  public String getDir() {
    return dir_;
  }

  public List<I_LibraryDependency> getLibraryDependencies() {
    //protect from external modification
    return new ArrayList<I_LibraryDependency>(libraryDependencies_);
  }
  
  public String getName() {
    return name_;
  }
  
  public List<I_Dependency> getNormalizedDependencies() {
    //protect from external modification
    return new ArrayList<I_Dependency>(normalizedDependencies_);
  }
  
  public List<I_ProjectDependency> getProjectDependencies() {
    //protect from external modification
    return new ArrayList<I_ProjectDependency>(projectDependencies_);
  }
  
  @Override
  public String getShortName() {
    if (name_ != null) {
      return new ProjectBrief(name_, version_).getShortName();
    }
    return null;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_Project#getStage(java.lang.String)
   */
  @Override
  public I_RoutineBrief getStage(String name) {
    return stages_.get(name);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_Project#getStages()
   */
  @Override
  public Map<String, I_RoutineBrief> getStages() {
    //protect from external modification
    return new HashMap<String, I_RoutineBrief>(stages_);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_Project#getTrait(java.lang.String)
   */
  @Override
  public I_RoutineBrief getTrait(String name) {
    return traits_.get(name);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_Project#getTraits()
   */
  @Override
  public Map<String, I_RoutineBrief> getTraits() {
    //protect from external modification
    return new HashMap<String, I_RoutineBrief>(traits_);
  }
  
  public String getVersion() {
    return version_;
  }
  
  @Override
  public int hashCode() {
    return hashCode(this);
  }

  @Override
  public String toString() {
    return toString(this);
  }
  
  public void setAttributes(Collection<? extends I_Parameter> attributes) {
    ParameterMutant.setMutants(attributes_, attributes);
  }
  
  public void setCommands(Collection<? extends I_RoutineBrief> commands) {
    commands_.clear();
    if (commands != null) {
      for (I_RoutineBrief command: commands) {
        addCommand(command);
      }
    }
  }
  
  public void setDependencies(Collection<? extends I_Dependency> dependencies) {
    dependencies_.clear();
    if (dependencies != null) {
      for (I_Dependency dep: dependencies) {
        addDependency(dep);
      }
    }
  }
  
  public void setDir(String dir) {
    this.dir_ = dir;
  }
  
  public void setLibraryDependencies(Collection<? extends I_LibraryDependency> libDependencies) {
    libraryDependencies_.clear();
    if (libDependencies != null) {
      for (I_LibraryDependency dep: libDependencies) {
        addLibraryDependency(dep);
      }
    }
  }
  
  public void setName(String name) {
    this.name_ = name;
  }
  
  public void setNormalizedDependencies(Collection<? extends I_Dependency> nDependencies) {
    normalizedDependencies_.clear();
    if (nDependencies != null) {
      for (I_Dependency dep: nDependencies) {
        addNormalizedDependency(dep);
      }
    }
  }
  
  public void setProjectDependencies(Collection<? extends I_ProjectDependency> projectDependencies) {
    projectDependencies_.clear();
    if (projectDependencies != null) {
      for (I_ProjectDependency dep: projectDependencies) {
        addProjectDependency(dep);
      }
    }
  }

  public void setStages(Collection<? extends I_RoutineBrief> stages) {
    stages_.clear();
    if (stages != null) {
      for (I_RoutineBrief stage: stages) {
        addStage(stage);
      }
    }
  }

  public void setTraits(Collection<? extends I_RoutineBrief> traits) {
    traits_.clear();
    if (traits != null) {
      for (I_RoutineBrief trait: traits) {
        addTrait(trait);
      }
    }
  }
  
  
  public void setVersion(String version) {
    this.version_ = version;
  }
  
  private void addProjectRoutineParents(Collection<ProjectRoutineType> routines, 
      Map<String,I_RoutineBrief> map, RoutineBriefOrigin origin) 
          throws IllegalArgumentException, ClassNotFoundException {
    if (routines != null) {
      for (ProjectRoutineType routine: routines) {
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
