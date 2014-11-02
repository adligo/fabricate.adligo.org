package org.adligo.fabricate.external.files;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.ThreadLocalPrintStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
  private I_FabContext ctx_;
  
  public FileUtils(I_FabContext ctx) {
    ctx_ = ctx;
  }
  public void removeRecursive(Path path) throws IOException
  {
      if (ctx_.isLogEnabled(FileUtils.class)) {
        ThreadLocalPrintStream.println("Deleting recursive " + path.toFile().getAbsolutePath());
      }
      Files.walkFileTree(path, new SimpleFileVisitor<Path>()
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
  public List<String> list(Path path, final I_FileMatcher matcher) throws IOException
  {
      final List<String> list = new ArrayList<String>();
      File pathFile = path.toFile();
      final String absPath = pathFile.getAbsolutePath();
      final String simplePathName = pathFile.getName();
      
      final int length = absPath.length();
      Files.walkFileTree(path, new SimpleFileVisitor<Path>()
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
}
