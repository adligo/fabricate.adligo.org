package org.adligo.fabricate.models.project;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class orders projects 
 * from those with no project dependencies
 * to projects with the most project dependencies.
 * 
 * @author scott
 *
 */
public class ProjectDependencyOrderer {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private List<String> names_ = new ArrayList<String>();
  private List<I_Project> projects_ = new ArrayList<I_Project>();
  
  public ProjectDependencyOrderer(List<I_Project> projects, I_FabSystem sys) {
    system_ = sys;
    log_ = sys.getLog();
    
    if (projects.size() == 0) {
      throw new IllegalArgumentException("projects");
    }
    if (projects.size() == 1) {
      I_Project prj = projects.get(0);
      names_.add(prj.getName());
      projects_.add(prj);
      return;
    }
    Iterator<I_Project> it0 = projects.iterator();
    Map<String,I_Project> alphaOrder = new TreeMap<String,I_Project>();
    while (it0.hasNext()) {
      I_Project proj = it0.next();
      alphaOrder.put(proj.getName(), proj);
    }
    
    List<I_Project> copy = new ArrayList<I_Project>(alphaOrder.values());
    Iterator<I_Project> it = copy.iterator();
    while (it.hasNext()) {
      I_Project proj = it.next();
      List<I_ProjectDependency> pdeps = proj.getProjectDependencies();
      if (log_.isLogEnabled(ProjectDependencyOrderer.class)) {
        log_.println(ProjectDependencyOrderer.class.getSimpleName() + ".init " + proj.getName() + " had " + pdeps.size());
      }
      if (pdeps.size() == 0) {
        names_.add(proj.getName());
        projects_.add(proj);
        it.remove();
      } 
    }
    if (log_.isLogEnabled(ProjectDependencyOrderer.class)) {
      log_.println(ProjectDependencyOrderer.class.getSimpleName() + ".init projects with out project dependency " + 
          names_.size() + system_.lineSeparator() + 
          names_);
    }
    int counter = 0;
    
    while (copy.size() > 0) {
      it = copy.iterator();
      counter++;
      if (counter >= 1000) {
        throw new IllegalStateException("1000 trys to order the projects by ");
      }
      logProgress(copy, counter);
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
        if (counter >= 5) {
          logDependentProjectsNotYetAddedForProject(proj, pdepCopy);
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


  private void logProgress(List<I_Project> copy, int counter) {
    if (log_.isLogEnabled(ProjectDependencyOrderer.class)) {
      List<String> left = new ArrayList<String>();
      for (I_Project proj: copy) {
        left.add(proj.getName());
      }
      log_.println(ProjectDependencyOrderer.class.getSimpleName() + ".init loop " + counter + 
          " projects with out project dependency is now " + 
          names_.size() + system_.lineSeparator() + 
          names_+ system_.lineSeparator() + 
          " the following projects are still left;" + system_.lineSeparator() + 
          left);
    }
  }


  public List<String> getNames() {
    return names_;
  }

  public List<I_Project> getProjects() {
    return projects_;
  }

  private void logDependentProjectsNotYetAddedForProject(I_Project proj,
      List<I_ProjectDependency> pdepCopy) {
    if (log_.isLogEnabled(ProjectDependencyOrderer.class)) {
      List<String> names = new ArrayList<String>();
      for (I_ProjectDependency pdep: pdepCopy) {
        names.add(pdep.getProjectName());
      }
      log_.println(ProjectDependencyOrderer.class.getSimpleName() + ".init " + proj.getName() + 
          " still has the following project dependencies which need to be added; " + 
          system_.lineSeparator() + 
          names);
    }
  }
  
}
