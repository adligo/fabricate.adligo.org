package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.common.DuplicateRoutineException;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.Parameter;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_LibraryDependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.dependencies.LibraryDependency;
import org.adligo.fabricate.models.dependencies.ProjectDependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project implements I_Project {
  /**
   * A unmodifiable list of Parameter instances.
   */
  private final List<I_Parameter> attributes_;
  /**
   * A unmodifiable list of String and RoutineBrief instances.
   */
  private final Map<String, I_RoutineBrief> commands_;
  
  /**
   * The absolute directory path
   */
  private final String dir_;
  
  private final String name_;
  private final String version_;
  private final List<I_Dependency> dependencies_;
  private final List<I_LibraryDependency> libraryDependencies_;
  private final List<I_Dependency> normalizedDependencies_;
  private final List<I_ProjectDependency> projectDependencies_;
  /**
   * A unmodifiable list of String and RoutineBrief instances.
   */
  private final Map<String, I_RoutineBrief> stages_;
  /**
   * A unmodifiable list of String and RoutineBrief instances.
   */
  private final Map<String, I_RoutineBrief> traits_;
  
  public Project(I_Project project) {
    dir_ = project.getDir();
    name_ = project.getName();
    version_ = project.getVersion();
    attributes_ = copyAttributes(project.getAttributes());
    commands_ = copyRoutines(project.getCommands(), RoutineBriefOrigin.PROJECT_COMMAND);
    dependencies_ = copyDependencies(project.getDependencies());
    libraryDependencies_ = copyLibraryDependencies(project.getLibraryDependencies());
    normalizedDependencies_ = copyDependencies(project.getNormalizedDependencies());
    projectDependencies_ = copyProjectDependencies(project.getProjectDependencies());
    stages_ = copyRoutines(project.getStages(), RoutineBriefOrigin.PROJECT_STAGE);
    traits_ = copyRoutines(project.getTraits(), RoutineBriefOrigin.PROJECT_TRAIT);
  }

  @Override
  public boolean equals(Object obj) {
    return ProjectMutant.equals(obj, this);
  }
  
  @Override
  public List<I_Parameter> getAttributes() {
    return attributes_;
  }

  @Override
  public List<I_Parameter> getAttributes(String key) {
    return ProjectMutant.getAttributes(key, attributes_);
  }
  
  @Override
  public I_RoutineBrief getCommand(String name) {
    return commands_.get(name);
  }

  @Override
  public Map<String, I_RoutineBrief> getCommands() {
    return commands_;
  }
  
  public String getDir() {
    return dir_;
  }
  
  @Override
  public String getDomainName() {
    if (name_ != null) {
      return new ProjectBrief(name_, version_).getDomainName();
    }
    return null;
  }
  
  public List<I_Dependency> getDependencies() {
    return dependencies_;
  }

  public List<I_LibraryDependency> getLibraryDependencies() {
    return libraryDependencies_;
  }
  
  public String getName() {
    return name_;
  }

  public List<I_Dependency> getNormalizedDependencies() {
    return normalizedDependencies_;
  }
  
  public List<I_ProjectDependency> getProjectDependencies() {
    return projectDependencies_;
  }

  @Override
  public String getShortName() {
    if (name_ != null) {
      return new ProjectBrief(name_, version_).getShortName();
    }
    return null;
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
  
  public String getVersion() {
    return version_;
  }

  @Override
  public int hashCode() {
    return ProjectMutant.hashCode(this);
  }
  
  @Override
  public String toString() {
    return ProjectMutant.toString(this);
  }
  
  private List<I_Parameter> copyAttributes(List<I_Parameter> attributes) {
    List<I_Parameter> copy = new ArrayList<I_Parameter>();
    if (attributes == null || attributes.size() == 0) {
      return Collections.emptyList();
    } else {
      for (I_Parameter attrib: attributes) {
        //don't dedupe
        if (attrib instanceof Parameter) {
          copy.add(attrib);
        } else {
          copy.add(new Parameter(attrib));
        }
      }
    }
    return Collections.unmodifiableList(copy);
  }
  
  private List<I_Dependency> copyDependencies(List<I_Dependency> dependencies) {
    List<I_Dependency> copy = new ArrayList<I_Dependency>();
    if (dependencies == null || dependencies.size() == 0) {
      return Collections.emptyList();
    } else {
      for (I_Dependency dep: dependencies) {
        if (!copy.contains(dep)) {
          if (dep instanceof Dependency) {
            copy.add(dep);
          } else {
            copy.add(new Dependency(dep));
          }
        }
      }
    }
    return Collections.unmodifiableList(copy);
  }

  private List<I_LibraryDependency> copyLibraryDependencies(List<I_LibraryDependency> libraryDependencies) {
    List<I_LibraryDependency> copy = new ArrayList<I_LibraryDependency>();
    
    if (libraryDependencies == null || libraryDependencies.size() == 0) {
      return Collections.emptyList();
    } else {
      for (I_LibraryDependency dep: libraryDependencies) {
        if (!copy.contains(dep)) {
          if (dep instanceof LibraryDependency) {
            copy.add(dep);
          } else {
            copy.add(new LibraryDependency(dep));
          }
        }
      }
    }
    return Collections.unmodifiableList(copy);
  }
  
  private List<I_ProjectDependency> copyProjectDependencies(List<I_ProjectDependency> projectDependencies) {
    List<I_ProjectDependency> copy = new ArrayList<I_ProjectDependency>();
    
    if (projectDependencies == null || projectDependencies.size() == 0) {
      return Collections.emptyList();
    } else {
      for (I_ProjectDependency dep: projectDependencies) {
        if (!copy.contains(dep)) {
          if (dep instanceof ProjectDependency) {
            copy.add(dep);
          } else {
            copy.add(new ProjectDependency(dep));
          }
        }
      }
    }
    return Collections.unmodifiableList(copy);
  }


  private Map<String, I_RoutineBrief> copyRoutines(Map<String, I_RoutineBrief> routines, RoutineBriefOrigin origin) {
    Map<String, I_RoutineBrief> copy = new HashMap<String, I_RoutineBrief>();
    
    if (routines == null || routines.size() == 0) {
      return Collections.emptyMap();
    } else {
      Collection<I_RoutineBrief> routineValues = routines.values();
      for (I_RoutineBrief routine: routineValues) {
        String name = routine.getName();
        if (copy.containsKey(name)) {
          DuplicateRoutineException dre = new DuplicateRoutineException();
          dre.setName(name);
          dre.setOrigin(routine.getOrigin());
          dre.setParentName(name_);
          throw dre;
        } else {
          if (routine instanceof RoutineBrief) {
            copy.put(name, routine);
          } else {
            copy.put(name, new RoutineBrief(routine));
          }
        }
      }
    }
    return Collections.unmodifiableMap(copy);
  }
  
  
}
