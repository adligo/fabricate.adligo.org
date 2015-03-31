package org.adligo.fabricate.models.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable variant of {@link I_ProjectModifications}.
 * 
 * @see I_ProjectModifications
 * @author scott
 *
 */
public class ProjectModificationsMutant implements I_ProjectModifications {
  private List<String> additions_ = new ArrayList<String>();
  private List<String> deletions_ = new ArrayList<String>();
  private List<String> modifications_ = new ArrayList<String>();
  private String name;
  
  public void addAddition(String addition) {
    if (addition != null) {
      if (!additions_.contains(addition)) {
        additions_.add(addition);
      }
    }
  }
  
  public void addDeletion(String deletion) {
    if (deletion != null) {
      if (!deletions_.contains(deletion)) {
        deletions_.add(deletion);
      }
    }
  }
  
  public void addModification(String modification) {
    if (modification != null) {
      if (!modifications_.contains(modification)) {
        modifications_.add(modification);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getAdditions()
   */
  @Override
  public List<String> getAdditions() {
    return new ArrayList<String>(additions_);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getDeletions()
   */
  @Override
  public List<String> getDeletions() {
    return new ArrayList<String>(deletions_);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getModifications()
   */
  @Override
  public List<String> getModifications() {
    return new ArrayList<String>(modifications_);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getName()
   */
  @Override
  public String getName() {
    return name;
  }
  
  public void setAdditions(List<String> adds) {
    additions_.clear();
    if (adds != null) {
      for (String add: adds) {
        addAddition(add);
      }
    }
  }
  
  
  public void setDeletions(List<String> dels) {
    deletions_.clear();
    if (dels != null) {
      for (String del: dels) {
        addDeletion(del);
      }
    }
  }
  
  public void setModifications(List<String> mods) {
    modifications_.clear();
    if (mods != null) {
      for (String mod: mods) {
        addModification(mod);
      }
    }
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
