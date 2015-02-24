package org.adligo.fabricate.routines;

/**
 * This is a marker interface that lets Fabricate
 * know that a particular routine is aware of the
 * projects.
 * @author scott
 *
 */
public interface I_ProjectProcessor {
  /**
   * The current project which is getting processed, 
   * or the last project to be processed (for failures).
   * @return
   */
  public String getCurrentProject();
}
