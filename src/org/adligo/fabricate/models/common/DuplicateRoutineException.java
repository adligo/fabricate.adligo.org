package org.adligo.fabricate.models.common;

/**
 * This exception is thrown when two routines with the same origin have the same name
 * in the same context.  By context I mean in a location like a xml file or the implicit 
 * location.  This intentionally keeps commands, facets, stages, traits and tasks unique
 * by their name.
 * 
 * 
 * @author scott
 *
 */
public class DuplicateRoutineException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * the origin of the routine that was a duplicate.
   */
  private RoutineBriefOrigin origin_;
  /**
   * the name of the routine that was a duplicate
   */
  private String name_;
  /**
   * the origin of the parent of the routine that was a duplicate.
   */
  private RoutineBriefOrigin parentOrigin_;
  /**
   * The name of the parent routine that was a duplicate,
   * or if the parentOrigin_ is null and the origin_ is PROJECT_* the project name.
   */
  private String parentName_;
  

  public RoutineBriefOrigin getOrigin() {
    return origin_;
  }

  public String getName() {
    return name_;
  }

  public RoutineBriefOrigin getParentOrigin() {
    return parentOrigin_;
  }

  public String getParentName() {
    return parentName_;
  }

  public void setOrigin(RoutineBriefOrigin origin) {
    this.origin_ = origin;
  }

  public void setName(String name) {
    this.name_ = name;
  }

  public void setParentOrigin(RoutineBriefOrigin parentOrigin) {
    this.parentOrigin_ = parentOrigin;
  }

  public void setParentName(String parentName) {
    this.parentName_ = parentName;
  }

}
