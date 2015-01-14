package org.adligo.fabricate.files;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.files.xml_io.I_FabXmlFiles;

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

public interface I_FabFiles extends I_FabXmlFiles {
  public I_FabContext getContext();

  public void setContext(I_FabContext ctx);
  /**
   * Stubs to new File(String filePath).exists();
   * 
   * @param filePath
   * @return
   */
  public boolean exists(String filePath);
  
  /**
   * Stubs to new File(String filePath).getAbsolutePath()
   * 
   * @param filePath
   * @return
   */
  public String getAbsolutePath(String filePath);
  /**
   * Stubs to new File(String dirsPath).mkDirs();
   * 
   * @param dirsPath
   * @return
   */
  public boolean mkdirs(String dirsPath);
  
  /**
   * This method lists the files under the path which match the file matcher.
   * @param path
   * @param matcher
   * @return
   * @throws IOException
   */
  public List<String> list(String path, final I_FileMatcher matcher) throws IOException;
  
  /**
   * This method deletes the files and folders under this path
   * and the path itself.
   * @param path
   * @throws IOException
   */
  public void removeRecursive(String path) throws IOException;
  
  /**
   * Calls new File(path).deleteOnExit();
   * @param path
   */
  public void deleteOnExit(String path);
  /**
   * Creates a new file
   * @param filePath
   * @return
   */
  public File create(String filePath) throws IOException;
  /**
   * 
   * @param filePath
   * @return
   */
//  public boolean move(String filePath);
  
}
