package org.adligo.fabricate.common.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * This is a way to stub out calls to the file system 
 * for test development.
 * 
 * @author scott
 *
 */

public interface I_FabFileIO {
  /**
   * calculate the md5 check sum of a file.
   * @param file
   * @return
   * @throws IOException
   */
  public String calculateMd5(String file) throws IOException;
  
  /**
   * @param url
   * @return the status code from the http gets response.
   * @throws IOException
   */
  public int check(String url) throws IOException;
  /**
   * Creates a new file;<br/>
   * new File(filePath).createNewFile();
   * @param filePath
   * @return
   */
  public File create(String filePath) throws IOException;
  
  /**
   * Delete the file now
   * @param path
   * @return
   */
  public void delete(String path) throws IOException ;
  
  /**
   * Calls new File(path).deleteOnExit();
   * @param path
   */
  public void deleteOnExit(String path);
  
  /**
   * This method deletes the files and folders under this path
   * and the path itself.
   * @param path
   * @throws IOException
   */
  public void deleteRecursive(String path) throws IOException;
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
   * simply creates a new ZipFile instance.
   * @param file
   * @return
   * @throws IOException
   */
  public ZipFile newZipFile(String file) throws IOException;
  
  /**
   * Read the content of a file
   * @param path
   * @return
   * @throws IOException
   */
  public String readFile(String path) throws IOException; 
  
  /**
   * unzips a zip file.
   * @param file
   * @param toDir
   * @throws IOException
   */
  public void unzip(String file, String toDir) throws IOException;
  
  /**
   * @param dir
   * @param zip
   * @return false when one of the zip file entries
   * is not on the disk under the dir.
   */
  public boolean verifyZipFileExtract(String dir, ZipFile zip);
  /**
   * This method writes a file out to disk from a input stream, using NIO 
   * (ByteBuffer, ReadableByteChannel, FileChannel).  It uses
   * a default buffer size of 16Kb.
   * @param in
   * @param fos
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos) throws IOException;
  /**
   * This method writes a file out to disk from a input stream, using NIO 
   * (ByteBuffer, ReadableByteChannel, FileChannel).
   * @param in
   * @param fos
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos, int bufferSize) 
      throws IOException;
  

  /**
   * 
   * @param filePath
   * @return
   */
//  public boolean move(String filePath);
  
}
