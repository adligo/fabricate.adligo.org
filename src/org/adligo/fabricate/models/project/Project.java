package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_LibraryDependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.dependencies.LibraryDependency;
import org.adligo.fabricate.models.dependencies.ProjectDependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project implements I_Project {
  /**
   * The absolute directory path
   */
  private final String dir_;
  
  private final String name_;
  private final String version_;
  private final List<I_Dependency> dependencies_;
  private final List<I_LibraryDependency> libraryDependencies_;
  private final List<I_ProjectDependency> projectDependencies_;
  
  public Project(I_Project project) {
    dir_ = project.getDir();
    name_ = project.getName();
    version_ = project.getVersion();
    dependencies_ = copyDependencies(project.getDependencies());
    libraryDependencies_ = copyLibraryDependencies(project.getLibraryDependencies());
    projectDependencies_ = copyProjectDependencies(project.getProjectDependencies());
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
  
  public List<I_Dependency> getDependencies() {
    return dependencies_;
  }

  public List<I_LibraryDependency> getLibraryDependencies() {
    return libraryDependencies_;
  }
  
  public List<I_ProjectDependency> getProjectDependencies() {
    return projectDependencies_;
  }
  
  public List<I_Dependency> copyDependencies(List<I_Dependency> dependencies) {
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

  public List<I_LibraryDependency> copyLibraryDependencies(List<I_LibraryDependency> libraryDependencies) {
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
  
  public List<I_ProjectDependency> copyProjectDependencies(List<I_ProjectDependency> projectDependencies) {
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
