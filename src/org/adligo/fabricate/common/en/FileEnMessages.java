package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_FileMessages;

/**
 * This class provides English about file system manipulation and discovery.
 * @author scott
 *
 */
public class FileEnMessages implements I_FileMessages {
  private static final String EXCLUDES = "Excludes: ";
 
  private static final String DID_NOT_MATCH_THE_FOLLOWING_PATTERN = 
      "did NOT match the following pattern;";
  private static final String MATCHED_THE_FOLLOWING_PATTERN = 
      "matched the following pattern;";
  private static final String FILE_MATCHING_PATTERNS_CAN_NOT_BE_EMPTY = 
      "File matching patterns may not be empty.";
  
  private static final String WILDCARD_IS_NOT_ALLOWED_IN_THE_MIDDLE_OF_A_FILE_PATTERN_FILE_NAME = 
      "The wildcard character (*) is not allowed in the middle of a file matching pattern file name.";
  private static final String WILDCARD_IS_NOT_ALLOWED_IN_THE_MIDDLE_OF_A_FILE_PATTERN_DIRECTORY_PATH = 
      "The wildcard character (*) is not allowed at the left or middle of a file matching pattern directory path.";
  private static final String INCLUDES = "Includes: ";
  
  private static final String SUBMITTING_A_HTTP_GET_TO_THE_FOLLOWING_URL_RETURNED_A_INVALID_STATUS_CODE_X =
      "Submitting a Http GET to the following url returned a invalid status code <X/>;";
  private static final String THE_FOLLOWING_FILE = "The following file;";
  
  private static final String THERE_WAS_A_PROBLEM_CREATING_THE_FOLLOWING_DIR = 
      "There was a problem creating the following directory;";
  private static final String THERE_WAS_A_PROBLEM_CREATING_THE_FOLLOWING_FILE = 
      "There was a problem creating the following file;";
  
  public static final FileEnMessages INSTANCE = new FileEnMessages();
    
  private FileEnMessages() {}
 
  public String getDidNotMatchedTheFollowingPattren() {
    return DID_NOT_MATCH_THE_FOLLOWING_PATTERN;
  }
  
  public String getIncludes() {
    return INCLUDES;
  }
  
  public String getExcludes() {
    return EXCLUDES;
  }
  
  public String getTheWildCardCharacterIsNotAllowedInMiddleFileName() {
    return WILDCARD_IS_NOT_ALLOWED_IN_THE_MIDDLE_OF_A_FILE_PATTERN_FILE_NAME;
  }
  
  public String getTheWildCardCharacterIsNotAllowedAtTheLeftOrMiddleDirectoryPath() {
    return WILDCARD_IS_NOT_ALLOWED_IN_THE_MIDDLE_OF_A_FILE_PATTERN_DIRECTORY_PATH;
  }
  
  public String getMatchedTheFollowingPattern() {
     return MATCHED_THE_FOLLOWING_PATTERN;
  }
  
  public String getTheFollowingFile() {
    return THE_FOLLOWING_FILE;
  }
  
  public String getThereWasAProblemCreatingTheFollowingDirectory() {
    return THERE_WAS_A_PROBLEM_CREATING_THE_FOLLOWING_DIR;
  }
  
  public String getThereWasAProblemCreatingTheFollowingFile() {
    return THERE_WAS_A_PROBLEM_CREATING_THE_FOLLOWING_FILE;
  }

  public String getFileMatchingPatternsMayNotBeEmpty() {
    return FILE_MATCHING_PATTERNS_CAN_NOT_BE_EMPTY;
  }
  
  public String getSubmittingAHttpGetToTheFollowingUrlReturnedAnInvalidStatusCodeX() {
    return SUBMITTING_A_HTTP_GET_TO_THE_FOLLOWING_URL_RETURNED_A_INVALID_STATUS_CODE_X;
  }
  
  
}
