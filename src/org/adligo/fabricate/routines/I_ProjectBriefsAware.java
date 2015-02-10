package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.project.I_ProjectBrief;

import java.util.Collection;
import java.util.List;

/**
 * This interface is used by Fabricate
 * to determine if a routine is aware of
 * the project briefs, and sets them 
 * if it is.
 * 
 * @author scott
 *
 */
public interface I_ProjectBriefsAware {
  public List<I_ProjectBrief> getProjectBriefs();
  public void setProjectBriefs(Collection<I_ProjectBrief> briefs);
}
