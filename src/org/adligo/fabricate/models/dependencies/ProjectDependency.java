package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependencyType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectDependency implements I_ProjectDependency {
  
  public static List<ProjectDependency> convert(Collection<ProjectDependencyType> deps) {
    List<ProjectDependency> toRet = new ArrayList<ProjectDependency>();
    if (deps != null) {
      for (ProjectDependencyType pdm: deps) {
        toRet.add(ProjectDependency.convert(pdm));
      }
    }
    return toRet;
  }
  
  public static ProjectDependency convert(ProjectDependencyType dep) {
    String platform = dep.getPlatform();
    String libName = dep.getValue();
    
    ProjectDependencyMutant ldm = new ProjectDependencyMutant();
    ldm.setProjectName(libName);
    ldm.setPlatform(platform);
    return new ProjectDependency(ldm);
  }
  
  private String projectName_;
  private String platform_;
  
  public ProjectDependency() {}
  
  public ProjectDependency(I_ProjectDependency other ) {
    projectName_ = other.getProjectName();
    platform_ = other.getPlatform();
  } 
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_ProjectDependency#getProjectName()
   */
  @Override
  public String getProjectName() {
    return projectName_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_ProjectDependency#getPlatform()
   */
  @Override
  public String getPlatform() {
    return platform_;
  }
  
  @Override
  public int hashCode() {
    return ProjectDependencyMutant.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return ProjectDependencyMutant.equals(obj, this);
  }

  @Override
  public String toString() {
    return ProjectDependencyMutant.toString(this);
  }
}
