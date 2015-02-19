package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_Fabrication;

/**
 * Implementations of this interface represent
 * bindings to a Scm (Source control management) system like Git.
 * They are expected to have a zero arg constructor so they
 * can easily be created through reflection.
 * 
 * @author scott
 *
 */
public interface I_ScmFactories extends I_Fabrication {

  /**
   * @returna factory whose routines obtain projects, 
   * for Git this is a alias for describe. 
   */
  public I_ScmFactory createGetVersionRoutine();
  /**
   * Tag the projects using the fabricate.xml
   * version numbers.  For git this is a alias
   * to branch.  Note by default this 
   * will look at the branchFilter xml key value
   * to determine which versions are suppose to 
   * become new branch labels.  By default 
   * this the prefix of 'b'.
   * @return
   */
  public I_ScmFactory createGrowBranchRoutine();
  /**
   * @returna factory whose routines obtain projects, 
   * for Git this is a alias for clone. The factory
   */
  public I_ScmFactory createObtainRoutine();
  /**
   * Share the current work in project group and it's projects
   * with others, for Git this is a alias to commit and push.
   * @return
   */
  public I_ScmFactory createShareRoutine();
  /**
   * Tag the projects using the fabricate.xml
   * version numbers.  For git this is a alias
   * to 'git tag -a'.
   * @return
   */
  public I_ScmFactory createTagVersionRoutine();
  /**
   * Update the project, for git this is a alias
   * to pull.
   * @return
   */
  public I_ScmFactory createUpdateRoutine();
}
