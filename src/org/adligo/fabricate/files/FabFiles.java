package org.adligo.fabricate.files;

import org.adligo.fabricate.build.stages.DefaultSetup;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.files.xml_io.FabXmlFiles;
import org.adligo.fabricate.files.xml_io.I_FabXmlFiles;
import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FabFiles implements I_FabFiles {
  public static final FabFiles INSTANCE = new FabFiles();
  private static I_FabXmlFiles XML_FILES = FabXmlFiles.INSTANCE;
  private I_FabContext ctx_;
  
  private FabFiles() {
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
  public DepotType parseDepot_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseDepot_v1_0(xmlFilePath);
  }

  
  @Override
  public FabricateDevType parseDev_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseDev_v1_0(xmlFilePath);
  }

  @Override
  public FabricateType parseFabricate_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseFabricate_v1_0(xmlFilePath);
  }

  @Override
  public FabricateProjectType parseProject_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseProject_v1_0(xmlFilePath);
  }

  @Override
  public LibraryType parseLibrary_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseLibrary_v1_0(xmlFilePath);
  }

  @Override
  public void writeDev_v1_0(String filePath, FabricateDevType dev) throws IOException {
    XML_FILES.writeDev_v1_0(filePath, dev);
  }

  @Override
  public void writeDepot_v1_0(String filePath, DepotType depot) throws IOException {
    XML_FILES.writeDepot_v1_0(filePath, depot);
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
  
  public I_FabContext getContext() {
    return ctx_;
  }

  public void setContext(I_FabContext ctx) {
    ctx_ = ctx;
  }

  private void log(String message) {
    if (ctx_ == null) {
      ThreadLocalPrintStream.println(message);
    } else if (ctx_.isLogEnabled(DefaultSetup.class)) {
      ThreadLocalPrintStream.println(message);
    }
  }

  

}
