package org.adligo.fabricate.models.scm;

/** 
 * A common interface for a source control management system
 * configuration information.
 * 
 * @author scott
 *
 */
public interface I_Scm {
  public static final String GIT = "Git";
  
  /**
   * The name of the SCM (i.e. a source control management system like Git, Cvs, Svn).
   * Note there are constants in this class for each SCM supported by 
   * fabricate.  
   * each 
   * @return
   */
  public String getName();
}
