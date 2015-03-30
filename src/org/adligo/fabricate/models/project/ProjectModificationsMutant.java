package org.adligo.fabricate.models.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectModificationsMutant implements I_ProjectModifications {
  private List<String> additions = new ArrayList<String>();
  private List<String> deletions = new ArrayList<String>();
  private List<String> modifications = new ArrayList<String>();

  
  public void addAddition(String addition) {
    additions.add(addition);
  }
  public void addDeletions(String deletion) {
    deletions.add(deletion);
  }
  public void addModifications(String modification) {
    modifications.add(modification);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getAdditions()
   */
  @Override
  public List<String> getAdditions() {
    return new ArrayList<String>(additions);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getDeletions()
   */
  @Override
  public List<String> getDeletions() {
    return new ArrayList<String>(deletions);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getModifications()
   */
  @Override
  public List<String> getModifications() {
    return new ArrayList<String>(modifications);
  }
}
