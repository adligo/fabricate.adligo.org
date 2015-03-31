package org.adligo.fabricate.models.project;

import org.adligo.fabricate.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Immutable variant of {@link I_ProjectModifications}.
 * 
 * @see I_ProjectModifications
 * @author scott
 *
 */
public class ProjectModifications implements I_ProjectModifications {
  private final List<String> additions_;
  private final List<String> deletions_;
  private final List<String> modifications_;
  private final String name_;
  
  public ProjectModifications(I_ProjectModifications other) {
    ProjectModificationsMutant filter = new ProjectModificationsMutant();
    
    List<String> otherAdds = other.getAdditions();
    if (otherAdds == null || otherAdds.size() == 0) {
      additions_ = Collections.emptyList();
    } else {
      filter.setAdditions(otherAdds);
      additions_ = Collections.unmodifiableList(filter.getAdditions());
    }
    List<String> otherDels = other.getDeletions();
    if (otherDels == null || otherDels.size() == 0) {
      deletions_ = Collections.emptyList();
    } else {
      filter.setDeletions(otherDels);
      deletions_ = Collections.unmodifiableList(filter.getDeletions());
    }
    List<String> otherMods = other.getModifications();
    if (otherMods == null || otherMods.size() == 0) {
      modifications_ = Collections.emptyList();
    } else {
      filter.setModifications(otherMods);
      modifications_ = Collections.unmodifiableList(filter.getModifications());
    }
    String name = other.getName();
    if (StringUtils.isEmpty(name)) {
      throw new IllegalArgumentException("name");
    }
    name_ = name;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getAdditions()
   */
  @Override
  public List<String> getAdditions() {
    return additions_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getDeletions()
   */
  @Override
  public List<String> getDeletions() {
    return deletions_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getModifications()
   */
  @Override
  public List<String> getModifications() {
    return modifications_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.project.I_ProjectModifications#getName()
   */
  @Override
  public String getName() {
    return name_;
  }
}
