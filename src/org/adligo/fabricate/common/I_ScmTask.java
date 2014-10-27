package org.adligo.fabricate.common;

/**
 * I_ScmTask is a interface to allow plug-able tasks
 * for fabricates stages.  It is generally assumed
 * that these will occur concurrently.
 * 
 * @author scott
 *
 */
public interface I_ScmTask {
  public void setup(String projectGroupFolder, I_Scm scm);
  public void interact(String projectName, String projectVersion);
}
