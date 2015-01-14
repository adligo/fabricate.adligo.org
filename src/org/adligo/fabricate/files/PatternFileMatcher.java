package org.adligo.fabricate.files;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.log.I_FabLog;

import java.io.File;

/**
 * This class provides a implementation of {@link I_FileMatcher} which matches
 * based on a pattern.   The pattern is applied to include files or exclude files.
 * The pattern may contain directory information and must contain file information.
 * File information may contain a wild card character (*) at the left or right 
 * of the file name.<br/>
 * <br/>
 * File Information Examples;<br/>
 * /*Test.java<br/>
 * \*Test.java<br/>
 * *Tests.java<br/>
 * <br/>
 * 
 * 
 * @see I_FileMatcher
 * @author scott
 *
 */
public class PatternFileMatcher implements I_FileMatcher {
  private final I_FabricateConstants constants_;
  
  private I_FabLog log_;
  private String fileSeparator_;
  private boolean anyDir_ = false;
  private String parentDir_;
  private boolean startFileWild_ = false;
  private boolean endFileWild_ = false;
  private String fileName_;
  private String pattern_;
  /**
   * includes or excludes
   */
  private boolean includes_;
  
  public PatternFileMatcher(I_FabLog log, String pattern, boolean includes) {
    log_ = log;
    constants_  = log.getConstants();
    pattern_ = pattern;
    includes_ = includes;
    int slash = pattern.indexOf("/");
    int nextSlash = pattern.indexOf("/", slash + 1);
    int lastSlash = nextSlash;
    if (nextSlash == -1 && slash != -1) {
      String beforeSlash = pattern.substring(0, slash);
      char [] chars = beforeSlash.toCharArray();
      boolean foundNonStar = false;
      for (int i = 0; i < chars.length; i++) {
        if (chars[i] != '*') {
          foundNonStar = true;
        }
      }
      if (!foundNonStar) {
        anyDir_ = true;
      }
      lastSlash = slash;
    } else if (slash != -1 && nextSlash != -1){
      
      nextSlash = pattern.indexOf("/", nextSlash + 1);
      while (nextSlash != -1) {
        lastSlash = nextSlash;
      }
      parentDir_ = pattern.substring(slash, lastSlash + 1);
      char [] chars = parentDir_.toCharArray();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        if (c == '/') {
          sb.append(File.separator);
        } else {
          sb.append(c);
        }
      }
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
          throw new IllegalArgumentException(fileMessages.getTheWildCardCharacterIsNotAllowedInMiddle());
        }
      } else {
        sb.append(c);
      }
    }
    fileName_ = sb.toString();
  }

  @Override
  public boolean isMatch(String path) {
    File file = new File(path);
    if (anyDir_) {
      String name = file.getName();
      if (startFileWild_) {
        if (endFileWild_) {
          if (name.indexOf(fileName_) == -1) {
            log(file, !includes_);
            return !includes_;
          } 
        } else {
          if (name.indexOf(fileName_) + fileName_.length() == name.length()) {
            log(file, includes_);
            return includes_;
          } 
        }
      } else if (endFileWild_) {
        if (name.indexOf(fileName_) == 0) {
          log(file, includes_);
          return includes_;
        }
      } else if (fileName_.equals(name)) {
        log(file, includes_);
        return includes_;
      }
    }
    log(file, !includes_);
    return !includes_;
  }

  public void log(File file, boolean matched) {
    I_FileMessages fileMessages = constants_.getFileMessages();
    if (log_.isLogEnabled(PatternFileMatcher.class)) {
      String message = fileMessages.getTheFollowingFile() + constants_.getLineSeperator() +
          file.getAbsolutePath() + constants_.getLineSeperator() +
          fileMessages.getDidNotMatchedTheFollowingPattren() + constants_.getLineSeperator() +
          pattern_;
      log_.println(message);
    }
  }

}
