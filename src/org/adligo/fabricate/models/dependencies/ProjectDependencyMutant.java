package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependencyType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectDependencyMutant implements I_ProjectDependency {
  
  public static List<ProjectDependencyMutant> convert(Collection<ProjectDependencyType> deps) {
    List<ProjectDependencyMutant> toRet = new ArrayList<ProjectDependencyMutant>();
    if (deps != null) {
      for (ProjectDependencyType pdm: deps) {
        toRet.add(ProjectDependencyMutant.convert(pdm));
      }
    }
    return toRet;
  }
  
  public static ProjectDependencyMutant convert(ProjectDependencyType dep) {
    String platform = dep.getPlatform();
    String libName = dep.getValue();
    
    ProjectDependencyMutant ldm = new ProjectDependencyMutant();
    ldm.setProjectName(libName);
    ldm.setPlatform(platform);
    return ldm;
  }
  
  public static boolean equals(Object obj, I_ProjectDependency dep) {
    if (dep == obj) 
      return true;
    if (obj instanceof I_ProjectDependency) {
      I_ProjectDependency other = (I_ProjectDependency) obj;
      String libName = dep.getProjectName();
      String oln = other.getProjectName();
      if (libName == null) {
        if (oln != null)
          return false;
      } else if (!libName.equals(oln))
        return false;
      
      String platformName = dep.getPlatform();
      String plat = other.getPlatform();
      if (platformName == null) {
        if (plat != null)
          return false;
      } else if (!platformName.equals(plat))
        return false;
      return true;
    }
    return false;
  }
  
  public static int hashCode(I_ProjectDependency dep) {
    final int prime = 31;
    int result = 1;
    String libraryName = dep.getProjectName();
    String platform = dep.getPlatform();
    
    result = prime * result + ((libraryName == null) ? 0 : libraryName.hashCode());
    result = prime * result + ((platform == null) ? 0 : platform.hashCode());
    return result;
  }
  
  public static String toString(I_ProjectDependency dep) {
    StringBuilder sb = new StringBuilder();
    sb.append(dep.getClass().getSimpleName());
    sb.append(" [name=");
    sb.append(dep.getProjectName());
    String platform = dep.getPlatform();
    if (platform != null) {
      sb.append(",platform=");
      sb.append(platform);
    }
    sb.append("]");
    return sb.toString();
  }
  
  
  private String projectName_;
  private String platform_;
  
  public ProjectDependencyMutant() {}
  
  public ProjectDependencyMutant(I_ProjectDependency other ) {
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
  
  public void setProjectName(String projectName) {
    this.projectName_ = projectName;
  }
  
  public void setPlatform(String platform) {
    this.platform_ = platform;
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
