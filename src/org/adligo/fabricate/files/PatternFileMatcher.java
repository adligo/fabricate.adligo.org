package org.adligo.fabricate.files;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.log.FabLog;
import org.adligo.fabricate.common.log.I_FabLog;

import java.io.File;

/**
 * This class provides a implementation of {@link I_FileMatcher} which matches
 * based on a pattern.   The pattern is defined with Unix directory name separation 
 * and is applied to include files or exclude files.  Unix directory name separation (/)
 * in the pattern name will match up to windows absolute directory name separtation 
 * in the isMatch method.
 * The pattern may contain directory information and must contain file information.
 * File information may contain a wild card character (*) at the left or right 
 * of the file name. Directory information may contain a wild card character (*)
 * at the right most directory only.<br/>
 * <br/>
 * File Information Examples;<br/>
 * 
 * /*Test.java<br/>
 * &#42;*Test.java<br/>
 * *Tests.java<br/>
 * Test*<br/>
 * <br/>
 * Directory and File Information Examples;<br/>
 * &#42;/*.java<br/>
 * src/&#42;/*.properties<br/>
 * src/i_log.properties<br/>
 * <br/>
 * @see I_FileMatcher
 * @author scott
 *
 */
public class PatternFileMatcher implements I_FileMatcher {
  private final I_FabricateConstants constants_;
  
  private I_FabFileIO files_;
  private String nameSeparator_;
  private I_FabLog log_;
  /**
   * The parent directory or null
   * if any directory is allowed.
   */
  private String parentDir_;
  /**
   * This means any directory under the parent Dir
   * or any directory at all if the parent directory is null.
   */
  private boolean anyDir_ = false;
  private boolean startFileWild_ = false;
  private boolean endFileWild_ = false;
  private String fileName_;
  private String pattern_;
  /**
   * includes or excludes
   */
  private boolean includes_;
  
  public PatternFileMatcher(I_FabFileIO fileIO, I_FabLog log, String pattern, boolean includes) {
    files_ = fileIO;
    nameSeparator_ = files_.getNameSeparator();
    
    log_ = log;
    constants_  = log.getConstants();
    if (pattern == null || pattern.trim().length() == 0) {
      I_FileMessages messages = constants_.getFileMessages();
      throw new IllegalArgumentException(messages.getFileMatchingPatternsMayNotBeEmpty());
    }
    pattern_ = pattern;
    includes_ = includes;
    int slash = pattern.indexOf("/");
    int nextSlash = pattern.indexOf("/", slash + 1);
    int lastSlash = pattern.lastIndexOf("/");
    
    if (nextSlash == -1 && slash != -1) {
      //just a single slash
      String beforeSlash = pattern.substring(0, slash);
      if (beforeSlash.length() == 1) {
        if ("*".equals(beforeSlash)) {
          anyDir_ = true;
        } else {
          parentDir_ = beforeSlash;
        }
      } else {
        char [] chars = beforeSlash.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
          char c = chars[i];
          if (i == chars.length - 1) {
            if ('*' == c) {
              anyDir_ = true;
            } else {
              sb.append(c);
            }
          } else if ('*' == c) {
            I_FileMessages fileMessages  =constants_.getFileMessages();
            throw new IllegalArgumentException(
                fileMessages.getTheWildCardCharacterIsNotAllowedAtTheLeftOrMiddleDirectoryPath());
          } else {
            sb.append(c);
          }
        }
        parentDir_ = sb.toString();
      }
    } else if (slash != -1 && nextSlash != -1){
      //multiple slashes
      String beforeLastSlash = pattern.substring(0, lastSlash);
      char [] chars = beforeLastSlash.toCharArray();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        if (i == chars.length - 1) {
          if ('*' == c) {
            anyDir_ = true;
          } else {
            sb.append(c);
          }
        } else if ('*' == c) {
          I_FileMessages fileMessages  =constants_.getFileMessages();
          throw new IllegalArgumentException(
              fileMessages.getTheWildCardCharacterIsNotAllowedAtTheLeftOrMiddleDirectoryPath());
        } else {
          if ('/' == c) {
            sb.append(nameSeparator_);
          } else {
            sb.append(c);
          }
        }
      }
      parentDir_ = sb.toString();
    }
    fileName_ = pattern.substring(lastSlash + 1, pattern.length());
    char [] chars = fileName_.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '*') {
        if (i == 0) {
          startFileWild_ = true;
        } else if (i == chars.length - 1) {
          endFileWild_ = true;
        } else {
          I_FileMessages fileMessages = constants_.getFileMessages();
          throw new IllegalArgumentException(fileMessages.getTheWildCardCharacterIsNotAllowedInMiddleFileName());
        }
      } else {
        sb.append(c);
      }
    }
    fileName_ = sb.toString();
  }

  @Override
  public boolean isMatch(String path) {
    File file = files_.instance(path);
    String fileName = file.getName();
    if (parentDir_ != null) {
      if (path.indexOf(parentDir_) == 0) {
        return matchFileName(file, fileName);
      } else {
        log(file,false);
        return false;
      }
    } else if (anyDir_ || path.indexOf(nameSeparator_) == -1){
      return matchFileName(file, fileName);
    } else {
      log(file, false);
      return false;
    }
    
  }

  public boolean matchFileName(File file, String fileName) {
    if (startFileWild_) {
      if (fileName.indexOf(fileName_) + fileName_.length() == fileName.length()) {
        log(file, true);
        return true;
      } else {
        log(file, false);
        return false;
      }
    } else if (endFileWild_) {
      if (fileName.indexOf(fileName_) == 0) {
        log(file, true);
        return true;
      } else {
        log(file, false);
        return false;
      }
    } else {
      if (fileName_.equals(fileName)) {
        log(file, true);
        return true;
      } else {
        log(file, false);
        return false;
      }
    }
  }

  public void log(File file, boolean matched) {
    I_FileMessages fileMessages = constants_.getFileMessages();
    if (log_.isLogEnabled(PatternFileMatcher.class)) {
      String includeExclude = fileMessages.getIncludes();
      if (!includes_) {
        includeExclude = fileMessages.getExcludes();
      }
      if (matched) {
        String message = fileMessages.getTheFollowingFile() + constants_.getLineSeperator() +
            file.getAbsolutePath() + constants_.getLineSeperator() +
            fileMessages.getMatchedTheFollowingPattern() + constants_.getLineSeperator() +
            FabLog.orderLine(constants_.isLeftToRight(), includeExclude, pattern_);
        log_.println(message);
      } else {
        String message = fileMessages.getTheFollowingFile() + constants_.getLineSeperator() +
            file.getAbsolutePath() + constants_.getLineSeperator() +
            fileMessages.getDidNotMatchedTheFollowingPattren() + constants_.getLineSeperator() +
            FabLog.orderLine(constants_.isLeftToRight(), includeExclude, pattern_);
        log_.println(message);
      }
    } 
  }

  public boolean isAnyDir() {
    return anyDir_;
  }
  
  public String getParentDir() {
    return parentDir_;
  }
}
