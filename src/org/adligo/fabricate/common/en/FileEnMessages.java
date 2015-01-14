package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_FileMessages;

/**
 * This class provides English about file system manipulation and discovery.
 * @author scott
 *
 */
public class FileEnMessages implements I_FileMessages {
  private static final String THE_FOLLOWING_FILE = "The following file;";
  private static final String DID_NOT_MATCH_THE_FOLLOWING_PATTERN = "did NOT match the following pattern;";
  private static final String MATCHED_THE_FOLLOWING_PATTERN = "matched the following pattern;";
  private static final String WILDCARD_IS_NOT_ALLOWED_IN_THE_MIDDLE_OF_A_FILE_PATTERN = 
      "The wildcard character (*) is not allowed in the middle of a file matching pattern.";
  public static final FileEnMessages INSTANCE = new FileEnMessages();
    

  private FileEnMessages() {}
 
  public String getTheWildCardCharacterIsNotAllowedInMiddle() {
    return WILDCARD_IS_NOT_ALLOWED_IN_THE_MIDDLE_OF_A_FILE_PATTERN;
  }
  
  public String getMatchedTheFollowingPattern() {
     return MATCHED_THE_FOLLOWING_PATTERN;
  }
  
  public String getDidNotMatchedTheFollowingPattren() {
    return DID_NOT_MATCH_THE_FOLLOWING_PATTERN;
  }
  
  public String getTheFollowingFile() {
    return THE_FOLLOWING_FILE;
  }
}
