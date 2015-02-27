package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;

import java.util.List;

/**
 * Just a marker interface to show
 * that a routine is aware of project briefs
 * (and there for may do something with or to projects).
 * Get the briefs from the I_Fabricate#getProjects() method.
 * @author scott
 *
 */
public interface I_ProjectBriefsAware extends I_FabricateAware, I_FabricationRoutine {
  public List<String> getProjectsToSkip();
  public void setProjectsToSkip(List<String> projectNames);
}
