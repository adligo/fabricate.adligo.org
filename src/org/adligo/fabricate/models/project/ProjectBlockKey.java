package org.adligo.fabricate.models.project;

import org.adligo.fabricate.common.util.StringUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class helps multiple threads communicate by keeping track of projects that are waiting
 * on other projects to finish before they start.  It is intended to be used
 * in other concurrent collections 
 * 
 * @author scott
 *
 */
public class ProjectBlockKey {
  /**
   * The project that is waiting to start.
   */
  private final String project_;
  /**
   * The project which must finish before the waiting project can start.
   */
  private final String blockingProject_;
  
  public ProjectBlockKey(String project, String blockingProject) {
    if (StringUtils.isEmpty(project)) {
      throw new IllegalArgumentException("project");
    }
    project_ = project;
    if (StringUtils.isEmpty(blockingProject)) {
      throw new IllegalArgumentException("blockingProject");
    }
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
    result = prime * result + blockingProject_.hashCode();
    result = prime * result + project_.hashCode();
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
    ProjectBlockKey other = (ProjectBlockKey) obj;
    if (!blockingProject_.equals(other.blockingProject_))
      return false;
    if (!project_.equals(other.project_))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "ProjectBlockKey [project=" + project_ + ", blockingProject=" + blockingProject_ + "]";
  }
}
