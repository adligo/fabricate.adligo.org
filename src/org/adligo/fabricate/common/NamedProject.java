package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

/**
 * a immutable wrapper around a FabricateProject
 * which includes the name of the directory it is 
 * stored in.
 * 
 * @author scott
 *
 */
public class NamedProject {
  private final String name_;
  private final FabricateProjectType project_;
  
  public NamedProject(String name, FabricateProjectType project) {
    name_ = name;
    project_ = project;
  }

  public String getName() {
    return name_;
  }

  public FabricateProjectType getProject() {
    return project_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NamedProject other = (NamedProject) obj;
    if (name_ == null) {
      if (other.name_ != null)
        return false;
    } else if (!name_.equals(other.name_))
      return false;
    return true;
  }
 
  public String toString() {
    return "NamedProject [name=" + name_ +"]";
  }
}
