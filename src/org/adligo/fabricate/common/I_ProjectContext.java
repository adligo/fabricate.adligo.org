package org.adligo.fabricate.common;

import java.util.Set;

/**
 * This will probably eventually contain
 * all information about a project, for now
 * it was a extract from Compile/Jar for
 * the FabricateClasspath2Eclipse command. 
 *   Eventually this will probably contain
 * a lot of information about the project/
 * fabricate run.
 * 
 * @author scott
 */
public interface I_ProjectContext {
  public String getProjectDir();
  public String getProjectName();
  public I_FabContext getFabContext();
  public Set<String> getProjectDependencies();
  public String getProjectJar(String project);
  public Set<String> getDependencies();
  public String getDependencyJar(String project);
}
