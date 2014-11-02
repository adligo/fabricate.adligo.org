package org.adligo.fabricate.common;

public class ProjectBlock {
  private String project_;
  private String blockingProject_;
  
  public ProjectBlock(String project, String blockingProject) {
    project_ = project;
    blockingProject_ = blockingProject;
  }

  public String getProject() {
    return project_;
  }

  public String getBlockingProject() {
    return blockingProject_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((blockingProject_ == null) ? 0 : blockingProject_.hashCode());
    result = prime * result + ((project_ == null) ? 0 : project_.hashCode());
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
    ProjectBlock other = (ProjectBlock) obj;
    if (blockingProject_ == null) {
      if (other.blockingProject_ != null)
        return false;
    } else if (!blockingProject_.equals(other.blockingProject_))
      return false;
    if (project_ == null) {
      if (other.project_ != null)
        return false;
    } else if (!project_.equals(other.project_))
      return false;
    return true;
  }
}
