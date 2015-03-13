package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_LibraryDependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.dependencies.LibraryDependencyMutant;
import org.adligo.fabricate.models.dependencies.ProjectDependencyMutant;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependenciesType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMutant implements I_Project {
  /**
   * actually only ParameterMutant instances.
   */
  private List<I_Parameter> attributes_ = 
      new ArrayList<I_Parameter>();
      
  /**
   * actually only RoutineBriefMutant instances.
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
   */
  private List<I_Dependency> dependencies_ = 
      new ArrayList<I_Dependency>();
      
  /**
   * actually only ProjectDependencyMutant instances
   */
  private List<I_LibraryDependency> libraryDependencies_ = 
          new ArrayList<I_LibraryDependency>();
          
  /**
   * actually only DependencyMutant instances.
   */
  private List<I_Dependency> normalizedDependencies_ = 
      new ArrayList<I_Dependency>();
      
  /**
   * actually only ProjectDependencyMutant instances
   */
  private List<I_ProjectDependency> projectDependencies_ = 
      new ArrayList<I_ProjectDependency>();
  
  /**
   * actually only RoutineBriefMutant instances.
   */
  private Map<String, I_RoutineBrief> stages_ =
      new HashMap<String, I_RoutineBrief>();
      
  /**
   * actually only RoutineBriefMutant instances.
   */
  private Map<String, I_RoutineBrief> traits_ =
      new HashMap<String, I_RoutineBrief>();
      
  public ProjectMutant() {}
  
  public ProjectMutant(I_Project project) {
    dir_ = project.getDir();
    name_ = project.getName();
    version_ = project.getVersion();
    setDependencies(project.getDependencies());
    setLibraryDependencies(project.getLibraryDependencies());
    setNormalizedDependencies(project.getNormalizedDependencies());
    setProjectDependencies(project.getProjectDependencies());
  }
  
  public ProjectMutant(String dir, ProjectType project, FabricateProjectType fabProject) {
    dir_ = dir;
    name_ = project.getName();
    version_ = project.getVersion();
    ProjectDependenciesType pdt = fabProject.getDependencies();
    if (pdt != null) {
      List<I_Dependency> deps =  DependencyMutant.convert(pdt.getDependency());
      setDependencies(deps);
      
      List<LibraryDependencyMutant> lDeps =  LibraryDependencyMutant.convert(pdt.getLibrary());
      setLibraryDependencies(lDeps);
      
      List<ProjectDependencyMutant> pDeps =  ProjectDependencyMutant.convert(pdt.getProject());
      setProjectDependencies(pDeps);  
    }
  }
  
  public void addAttribute(I_Parameter parameter) {
    //don't dedup allow multiple attributes_
    if (attributes_ instanceof ParameterMutant) {
      attributes_.add(parameter);
    } else if (parameter != null) {
      attributes_.add(new ParameterMutant(parameter));
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
          
  public String getDir() {
    return dir_;
  }
  
  public String getName() {
    return name_;
  }
  
  public String getVersion() {
    return version_;
  }
  
  public void setDir(String dir) {
    this.dir_ = dir;
  }
  
  public void setName(String name) {
    this.name_ = name;
  }
  
  public void setVersion(String version) {
    this.version_ = version;
  }

  public List<I_Parameter> getAttributes() {
    return attributes_;
  }
  
  public List<I_Dependency> getDependencies() {
    return dependencies_;
  }

  public List<I_LibraryDependency> getLibraryDependencies() {
    return libraryDependencies_;
  }

  public List<I_Dependency> getNormalizedDependencies() {
    return normalizedDependencies_;
  }
  
  public List<I_ProjectDependency> getProjectDependencies() {
    return projectDependencies_;
  }
  
  public void setAttributes(Collection<? extends I_Parameter> attributes) {
    attributes_.clear();
    if (attributes != null) {
      for (I_Parameter attribute: attributes) {
        addAttribute(attribute);
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

  public void setLibraryDependencies(Collection<? extends I_LibraryDependency> libDependencies) {
    libraryDependencies_.clear();
    if (libDependencies != null) {
      for (I_LibraryDependency dep: libDependencies) {
        addLibraryDependency(dep);
      }
    }
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

  @Override
  public String getShortName() {
    if (name_ != null) {
      return new ProjectBrief(name_, version_).getShortName();
    }
    return null;
  }

  @Override
  public String getDomainName() {
    if (name_ != null) {
      return new ProjectBrief(name_, version_).getDomainName();
    }
    return null;
  }
  
}
