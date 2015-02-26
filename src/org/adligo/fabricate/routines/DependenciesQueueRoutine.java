package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.project.I_Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class provides a Queue of projects which can help implementers 
 * process projects concurrently in project dependency order which is
 * fairly tricky, because routine instances must wait on other 
 * specific routine instances.
 * 
 * @diagram_sync on 1/26/2014 with Overview.seq
 * @author scott
 *
 */
public abstract class DependenciesQueueRoutine extends AbstractRoutine implements 
  I_ConcurrencyAware, I_FabricationRoutine, I_ProjectsAware {
  protected List<I_Project> projects_;
  
  public List<I_Project> getProjects() {
    return projects_;
  }
  
  public void setProjects(Collection<I_Project> projects) {
    List<I_Project> copy = new ArrayList<I_Project>();
    //TODO immutables
    copy.addAll(projects);
    projects = Collections.unmodifiableList(copy);
  }

  
}
