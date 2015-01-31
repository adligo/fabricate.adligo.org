package org.adligo.fabricate.common.i18n;

/**
 * This interface provides a interchangeable way to obtain language about file system manipulation and discovery.
 * @author scott
 */
public interface I_FileMessages {
  public String getDidNotMatchedTheFollowingPattren();
  public String getExcludes();
  public String getIncludes();
  public String getMatchedTheFollowingPattern();
  public String getSubmittingAHttpGetToTheFollowingUrlReturnedAnInvalidStatusCodeX();
  public String getTheFollowingFile();
  public String getThereWasAProblemCreatingTheFollowingFile();
  public String getTheWildCardCharacterIsNotAllowedInMiddleFileName();
  public String getTheWildCardCharacterIsNotAllowedAtTheLeftOrMiddleDirectoryPath();
  public String getFileMatchingPatternsMayNotBeEmpty();
  
}