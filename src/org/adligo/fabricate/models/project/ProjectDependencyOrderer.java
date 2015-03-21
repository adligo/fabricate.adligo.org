package org.adligo.fabricate.models.project;

import org.adligo.fabricate.models.dependencies.I_ProjectDependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class orders projects 
 * from those with no project dependencies
 * to projects with the most project dependencies.
 * 
 * @author scott
 *
 */
public class ProjectDependencyOrderer {
  private List<String> names_ = new ArrayList<String>();
  private List<I_Project> projects_ = new ArrayList<I_Project>();
  
  public ProjectDependencyOrderer(List<I_Project> project) {
    List<I_Project> copy = new ArrayList<I_Project>(project);
    Iterator<I_Project> it = copy.iterator();
    while (it.hasNext()) {
      I_Project proj = it.next();
      List<I_ProjectDependency> pdeps = proj.getProjectDependencies();
      if (pdeps.size() == 0) {
        names_.add(proj.getName());
        projects_.add(proj);
        it.remove();
      }
    }
    while (copy.size() > 0) {
      it = copy.iterator();
      
      while (it.hasNext()) {
        I_Project proj = it.next();
        List<I_ProjectDependency> pdeps = proj.getProjectDependencies();
        
        List<I_ProjectDependency> pdepCopy = new ArrayList<I_ProjectDependency>(pdeps);
        Iterator<I_ProjectDependency> pit = pdepCopy.iterator();
        while (pit.hasNext()) {
          I_ProjectDependency pd = pit.next();
          String name = pd.getProjectName();
          if (names_.contains(name)) {
            pit.remove();
          }
        }
        if (pdepCopy.size() == 0) {
          names_.add(proj.getName());
          projects_.add(proj);
          it.remove();
        }
      }
    }
    names_ = Collections.unmodifiableList(names_);
    projects_ = Collections.unmodifiableList(projects_);
  }

  public List<String> getNames() {
    return names_;
  }

  public List<I_Project> getProjects() {
    return projects_;
  }
  
  
}
