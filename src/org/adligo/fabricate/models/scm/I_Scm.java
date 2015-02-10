package org.adligo.fabricate.models.scm;

/** 
 * a marker interface for a source control management system
 * @author scott
 *
 */
public interface I_Scm {
  /**
   * The name of the scm (i.e. Git, Cvs, Svn)
   * @return
   */
  public String getName();
}
