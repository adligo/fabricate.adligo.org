package org.adligo.fabricate.common.files;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This is a way to stub out calls to the file system 
 * for test development.
 * 
 * @author scott
 *
 */

public interface I_FabFileIO {
  /**
   * Creates a new file;<br/>
   * new File(filePath).createNewFile();
   * @param filePath
   * @return
   */
  public File create(String filePath) throws IOException;
  
  /**
   * Calls new File(path).deleteOnExit();
   * @param path
   */
  public void deleteOnExit(String path);
  
  /**
   * This method downloads a file from the url
   * to the file path (may be relative or absolute).
   * 
   * @param url
   * @param file
   * @throws IOException
   */
  public void downloadFile(String url, String file) throws IOException;

  /**
   * @param filePath
   * @return
   * new File(String filePath).exists();
   */
  public boolean exists(String filePath);
  
  /**
   * 
   * @param filePath
   * @return new File(String filePath).getAbsolutePath()
   * the system dependent path.
   */
  public String getAbsolutePath(String filePath);
  /**
   * 
   * @return the system dependent directory
   * separator (Unix and Mac '\', Windows '/')
   */
  public String getNameSeparator();
  /**
   * 
   * @param absolutePath a system dependent 
   * path.
   * @return a path which contains / for path separators,
   * for Unix (Mac) paths this returns a unchanged value.
   * For Windows paths this will return a strange value;<br/>
   * Input;<br/>
   * C:\foo\bar\etc<br/>
   * Output;<br/>
   * C:/foo/bar/etc<br/>
   */
  public String getSlashPath(String absolutePath);

  
  /**
   * Creates a File instance;<br/>
   * return new File(filePath);
   * @param filePath
   * @return
   */
  public File instance(String filePath);
  /**
   * This method lists the files under the path which match the file matcher.
   * @param path
   * @param matcher
   * @return The list of absolute path names.
   * @throws IOException
   */
  public List<String> list(String path, final I_FileMatcher matcher) throws IOException;
  
  
  /**
   * @param dirsPath
   * new File(String dirsPath).mkDirs();
   * @return
   */
  public boolean mkdirs(String dirsPath);
  
  /**
   * Read the content of a file
   * @param path
   * @return
   * @throws IOException
   */
  public String readFile(String path) throws IOException; 
  /**
   * This method deletes the files and folders under this path
   * and the path itself.
   * @param path
   * @throws IOException
   */
  public void removeRecursive(String path) throws IOException;
  
  

  /**
   * 
   * @param filePath
   * @return
   */
//  public boolean move(String filePath);
  
}
