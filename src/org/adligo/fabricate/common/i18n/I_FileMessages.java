package org.adligo.fabricate.common.i18n;

/**
 * This interface provides a interchangeable way to obtain language about file system manipulation and discovery.
 * @author scott
 */
public interface I_FileMessages {
  
  public String getDidNotMatchedTheFollowingPattren();
  public String getExcludes();
  public String getFileMatchingPatternsMayNotBeEmpty();
  public String getFinisedDownloadFromX();
  
  public String getIncludes();
  public String getMatchedTheFollowingPattern();
  
  
  public String getStartingDownloadFromX();
  public String getSubmittingAHttpGetToTheFollowingUrlReturnedAnInvalidStatusCodeX();
  public String getTheFollowingDownloadIsXPercentComplete();
  public String getTheFollowingFile();
  
  public String getThereWasAProblemCreatingTheFollowingDirectory();
  public String getThereWasAProblemCreatingTheFollowingFile();
  public String getThereWasAProblemDeletingTheFollowingFile();
  
  public String getTheWildCardCharacterIsNotAllowedInMiddleFileName();
  public String getTheWildCardCharacterIsNotAllowedAtTheLeftOrMiddleDirectoryPath();
  
  
  
}