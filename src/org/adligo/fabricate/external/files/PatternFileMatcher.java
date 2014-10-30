package org.adligo.fabricate.external.files;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.ThreadLocalPrintStream;

import java.io.File;

public class PatternFileMatcher implements I_FileMatcher {
  private I_FabContext ctx_;
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
  
  public PatternFileMatcher(I_FabContext ctx, String pattern, boolean includes) {
    ctx_ = ctx;
    pattern_ = pattern;
    includes_ = includes;
    int slash = pattern.indexOf("/");
    int nextSlash = pattern.indexOf("/", slash + 1);
    int lastSlash = nextSlash;
    if (nextSlash == -1) {
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
    } else {
      
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
          throw new IllegalArgumentException(
              "Wildcard is not allowed in the middle of file name text.");
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
    String match = "matched";
    if (!matched) {
      match = "did NOT match";
    }
    if (ctx_.isLogEnabled(PatternFileMatcher.class)) {
      ThreadLocalPrintStream.println("The following file " + match + " the pattern " + 
            pattern_ + ", includes " + includes_ + File.separator +
            "/t" + file.getAbsolutePath());
    }
  }
  
}
