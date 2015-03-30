package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.project.I_Project;

import java.util.Collection;
import java.util.List;

/**
 * This is a marker interface that lets Fabricate
 * know that a particular routine is aware of the
 * projects.
 * @author scott
 *
 */
public interface I_ProjectsAware extends I_FabricationRoutine {
  public List<I_Project> getProjects();
  public void setProjects(Collection<I_Project> projects);
}
