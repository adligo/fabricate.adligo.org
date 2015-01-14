package org.adligo.fabricate.files;

public interface I_FileMatcher {
  /**
   * if this file is is a match for this matcher. 
   * @param relativeAbsolutePath may seem like a oxymoron,
   * it refers to a absolutePath (using the native File.seperator chars)
   * which is relative to some other absolutePath.
   * Ie if the absolutePath was /Volumes/foo/bar/src/foo.xml
   * the relative absolutePath could be src/foo.xml.
   * 
   * @return
   */
  public boolean isMatch(String relativeAbsolutePath);
}
