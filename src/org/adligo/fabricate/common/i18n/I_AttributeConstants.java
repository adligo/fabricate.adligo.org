package org.adligo.fabricate.common.i18n;

/**
 * This class contains attribute keys
 * for project.xml or fabricate.xml attributes.
 * 
 * @author scott
 *
 */
public interface I_AttributeConstants {

  /**
   * This is the key to a relative project directory
   * which has one or more nested parameter 
   * for which files should match and get excluded from the jar.
   * Note .class files are never removed.
   * @return
   */
  public abstract String getExclude();

  /**
   * This is the key to a value under a includes or excludes 
   * parameter which identifies files to include or exclude.
   * It may contain values with relative paths 
   * wildcards (i.e. *&#47;*.xml) or the full relative path name 
   * (Note &#47; is translated to \ on Windows ).
   * @return
   */
  public String getFiles();
  
  /**
   * The key to a attribute for the default git branch
   * for all projects (fabricate.xml) or a specific project (project.xml).
   * This defaults to the value 'master' if no attribute
   * was found for this key.
   * @return
   */
  public abstract String getGitDefaultBranch();

  /**
   * This is the key to a relative project directory
   * which has one or more nested parameter 
   * for which files should match and get included in the jar.
   * @return
   */
  public abstract String getInclude();

}