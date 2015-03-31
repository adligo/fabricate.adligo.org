package org.adligo.fabricate.models.project;

import java.util.List;

/**
 * This interface represents modifications made to a project, which 
 * were discovered through use of the SCM tool (i.e. git status).
 *  
 * @author scott
 *
 */
public interface I_ProjectModifications {
  
  /**
   * This is a list of relative system dependent paths
   * of new files for a project.
   * @return
   */
  public abstract List<String> getAdditions();
  /**
   * This is a list of relative system dependent paths
   * of files which were removed from a project.
   * @return
   */
  public abstract List<String> getDeletions();
  /**
   * This is a list of relative system dependent paths
   * of files which were modified in a project.  This
   * list does NOT include the additions or deletions.
   * @return
   */
  public abstract List<String> getModifications();

  /**
   * The name of the project.
   * @return
   */
  public String getName();
}