package org.adligo.fabricate.files;

import org.adligo.fabricate.files.xml_io.I_FabXmlFiles;

/**
 * This is a way to stub out calls to the file system 
 * for test development.
 * 
 * @author scott
 *
 */

public interface I_FabFiles extends I_FabXmlFiles {
  /**
   * Stubs to new File(String filePath).exists();
   * 
   * @param dirsPath
   * @return
   */
  public boolean exists(String filePath);
  /**
   * Stubs to new File(String dirsPath).mkDirs();
   * 
   * @param dirsPath
   * @return
   */
  public boolean mkdirs(String dirsPath);
  /**
   * Creates a new file
   * @param filePath
   * @return
   */
//  public File create(String filePath);
  /**
   * 
   * @param filePath
   * @return
   */
//  public boolean move(String filePath);
  
}
