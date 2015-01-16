package org.adligo.fabricate.files;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FabFileIO implements I_FabFileIO {
  public static final FabFileIO INSTANCE = new FabFileIO();
  private I_FabLog log_;
  
  private FabFileIO() {
  }
  
  @Override
  public File create(String filePath) throws IOException {
    File file = new File(filePath);
    if (file.createNewFile()) {
      return file;
    }
    return null;
  }
  
  @Override
  public void deleteOnExit(String path) {
    new File(path).deleteOnExit();
  }

  
  @Override
  public boolean exists(String filePath) {
    return new File(filePath).exists();
  }

  @Override
  public boolean mkdirs(String dirsPath) {
    return new File(dirsPath).mkdirs();
  }

  @Override
  public String getAbsolutePath(String filePath) {
    return new File(filePath).getAbsolutePath();
  }

  public void removeRecursive(String path) throws IOException
  {
      File dir = new File(path);
      Path dirPath = dir.toPath();
      log("Deleting recursive " + dir.getAbsolutePath());
      Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>()
      {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                  throws IOException
          {
              Files.delete(file);
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
          {
              // try to delete the file anyway, even if its attributes
              // could not be read, since delete-only access is
              // theoretically possible
              Files.delete(file);
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
          {
              if (exc == null)
              {
                  Files.delete(dir);
                  return FileVisitResult.CONTINUE;
              }
              else
              {
                  // directory iteration failed; propagate exception
                  throw exc;
              }
          }
      });
  }
  
  /**
   * 
   * @param path
   * @param matcher
   * @param ctx
   * @return a list of relative absolute paths to the path parameter
   * which match in the matcher parameter.
   * 
   * @throws IOException
   */
  public List<String> list(String path, final I_FileMatcher matcher) throws IOException
  {
      Path dir = new File(path).toPath();
      final List<String> list = new ArrayList<String>();
      File pathFile = dir.toFile();
      final String absPath = pathFile.getAbsolutePath();
      final String simplePathName = pathFile.getName();
      
      final int length = absPath.length();
      Files.walkFileTree(dir, new SimpleFileVisitor<Path>()
      {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                  throws IOException
          {
              File f = file.toFile();
              String absPath = f.getAbsolutePath();
              absPath = simplePathName + 
                  absPath.substring(length, absPath.length());
              if (matcher.isMatch(absPath)) {
                list.add(absPath);
              }
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
          {
            // this should never occur, unless some permission issue causes it
            if (exc != null) {
              throw exc;
            }
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
          {
            // this should never occur, unless some permission issue causes it
            if (exc != null) {
              throw exc;
            }
            return FileVisitResult.CONTINUE;
          }
      });
      return list;
  }
  
  private void log(String message) {
    if (log_ == null) {
      ThreadLocalPrintStream.println(message);
    } else if (log_.isLogEnabled(FabFileIO.class)) {
      log_.println(message);
    }
  }

  @Override
  public I_FabLog getLog() {
    return log_;
  }

  @Override
  public void setLog(I_FabLog log) {
    log_ = log;
  }

  @Override
  public String getNameSeparator() {
    return File.separator;
  }

  @Override
  public String getSlashPath(String absolutePath) {
    char [] chars = absolutePath.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '\\') {
          sb.append("/");
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  @Override
  public File instance(String filePath) {
    return new File(filePath);
  }
}
