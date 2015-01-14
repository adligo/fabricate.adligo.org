package org.adligo.fabricate.files;

public interface I_FileMatcher {
  /**
   * This interface provides a method to check if a file is is a match for this matcher. 
   * @param relativeSystemPath 
   * is a relative or absolute system specific path (i.e. /dev/null for
   * unix based systems or C:/dev/null for Windows). <br/>
   * <br/>
   * Examples;<br/>
   * <br/>
   * A) On Unix (Mac) a absolutePath is;<br/>
   * /Volumes/foo/bar/src/foo.xml<br/>
   * a relative relativeSystemPath is;<br/>
   * src/foo.xml<br/>
   * for a java program running in;<br/>
   * /Volumes/foo/bar<br/>
   * <br/>
   * B) On Windows a absolutePath is;<br/>
   * C:\foo\bar\src\foo.xml<br/>
   * a relative relativeSystemPath is;<br/>
   * src\foo.xml<br/>
   * for a java program running in;<br/>
   * C:\foo\bar<br/>
   * @return
   */
  public boolean isMatch(String relativeSystemPath);
}
