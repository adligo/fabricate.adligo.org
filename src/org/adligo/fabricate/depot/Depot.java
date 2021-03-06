package org.adligo.fabricate.depot;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.depot_v1_0.ArtifactType;
import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class manages the depot directory 
 * which consists of a depot.xml file and a some sub
 * directories.  Only one fabricate process may be 
 * using a fabricate depot directory at a time (fabricate puts a .running 
 * file in the depot directory and deletes it when it is finished).
 * 
 * Concurrent write access is generally used by this Depot implementation
 * to all other threads to concurrently add entries to the depot.  Removing
 * entries should be done in one of two ways;
 * 1) during a clean of a depot directory, which id done on the main
 *    fabricate thread (usually by the DefaultSetup class)
 * 2) when a project is replacing it's own artifact,
 *     which should only happen when a single project is getting fabricated.
 *     (or in other words the fab shell script was called from a directory with
 *     a project.xml). This is also usually done by DefaultSetup.
 * 
 * However only one 
 * @author scott
 *
 */
public class Depot implements I_Depot {
  private final I_FabSystem system_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private String dir_;
  private I_FabLog log_;
  /**
   * This is the main runtime index of what is in the 
   * depot directory, it is generally created
   * at instance construction time, and updated
   * as new things are added to the depot directory
   */
  private ConcurrentHashMap<ArtifactKey, String> artifactKeysToArtifacts_ = 
      new ConcurrentHashMap<ArtifactKey, String>();
  
  public Depot(String dir, DepotContext ctx, DepotType depot) {
    dir_ = dir;
    system_ = ctx.getSystem();
    files_ = ctx.getFiles();
    
    xmlFiles_ = ctx.getXmlFiles();
    log_ = ctx.getLog();
    List<ArtifactType> artifacts = depot.getArtifact();
    
    if (artifacts != null) {
      for (ArtifactType art: artifacts) {
        String platform = art.getPlatform();
        if (platform != null) {
          platform = platform.toLowerCase();
        } else {
          platform = "jse";
        }
        String project = art.getProject();
        String file = art.getFilename();
        String type = art.getType();
        
        String artDir = getArtifactDir(type, platform);
        String depotFileName = getDepotFile(artDir, file);
        artifactKeysToArtifacts_.put(new ArtifactKey(project, type, platform), depotFileName);
      }
    }
    if (log_.isLogEnabled(Depot.class)) {
      log_.println("Creating depot with " + artifactKeysToArtifacts_.size() + " artifacts.");
    }
  }

  @Override
  public synchronized boolean add(String externalFile, I_Artifact entryData) {
    String projectName = entryData.getProjectName();
    if (StringUtils.isEmpty(projectName)) {
      throw new IllegalArgumentException("No project name!");
    }
    String artifactType = entryData.getType();
    if (StringUtils.isEmpty(artifactType)) {
      throw new IllegalArgumentException("No artifact type!");
    }
    String platform = entryData.getPlatformName();
    if (StringUtils.isEmpty(platform)) {
      throw new IllegalArgumentException("No platform!");
    }
    if (!files_.exists(externalFile)) {
      throw new IllegalArgumentException("No external file!");
    }
    String artDir = null;
    
    artDir = getArtifactDir(artifactType, platform);
    if (!files_.exists(artDir)) {
      if (!files_.mkdirs(artDir)) {
        I_FabricateConstants constants = system_.getConstants();
        I_SystemMessages messages = constants.getSystemMessages();
        String message = messages.getThereWasAProblemCreatingTheFollowingDirectory();
        throw new IllegalStateException(message + system_.lineSeparator() +
            artDir);
      }
    }
    String fileName = entryData.getFileName();
    String depotFileName = getDepotFile(artDir, fileName);
    
    if (files_.exists(depotFileName)) {
      throw new IllegalStateException("The following file already exists in the depot." + System.lineSeparator() +
          "\t" + depotFileName);
    }
    if (log_.isLogEnabled(Depot.class)) {
      log_.println("Moving file " + externalFile + System.lineSeparator() +
            "\tto " + depotFileName + System.lineSeparator());
    }
    try {
      files_.move(externalFile, depotFileName, StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    } 
    //ok the file is in
    String platformName = getPlatformLower(platform);
    ArtifactKey aKey = new ArtifactKey(projectName, artifactType, platformName);
    artifactKeysToArtifacts_.put(aKey, depotFileName);
    
    if (log_.isLogEnabled(Depot.class)) {
      log_.println("Depot now has " + artifactType + "/" + projectName + "/" + fileName);
    }
    return true;
  }

  @Override
  public boolean deleteIfPresent(I_Artifact entryData) {
    String projectName = entryData.getProjectName();
    if (StringUtils.isEmpty(projectName)) {
      throw new IllegalArgumentException("No project name!");
    }
    String artifactType = entryData.getType();
    if (StringUtils.isEmpty(artifactType)) {
      throw new IllegalArgumentException("No artifact type!");
    }
    String platform = entryData.getPlatformName();
    if (StringUtils.isEmpty(platform)) {
      throw new IllegalArgumentException("No platform type!");
    }
    String artDir = null;
    
    artDir = getArtifactDir(artifactType, platform);
    
    String fileName = entryData.getFileName();
    String depotFileName = getDepotFile(artDir, fileName);
    if (log_.isLogEnabled(Depot.class)) {
      log_.println("Checking if should delete file " + depotFileName);
    }
    if (files_.exists(depotFileName)) {
      if (log_.isLogEnabled(Depot.class)) {
        log_.println("Deleting file " + depotFileName);
      }
      try {
        files_.delete(depotFileName);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      } 
    }
    String platformName = getPlatformLower(platform);
    ArtifactKey aKey = new ArtifactKey(projectName, artifactType, platformName);
    artifactKeysToArtifacts_.remove(aKey);
    
    if (log_.isLogEnabled(Depot.class)) {
      log_.println("Depot now has " + artifactType + "/" + projectName + "/" + depotFileName);
    }
    return true;
  }
  private String getDepotFile(String artDir, String fileName) {
    String depotFileName = artDir + files_.getNameSeparator() + fileName;
    return depotFileName;
  }

  private String getArtifactDir(String artifactType, String platform) {
    String artDir;
    platform = platform.toLowerCase();
    
    if ("jse".equalsIgnoreCase(platform)) {
      artDir = dir_ + files_.getNameSeparator() + artifactType + "s";
    } else {
      artDir = dir_ + files_.getNameSeparator() + platform + "_" + artifactType + "s";
    }
    return artDir;
  }

  private String getPlatformLower(String platform) {
    String artDir;
    platform = platform.toLowerCase();
    if ("jse".equalsIgnoreCase(platform) || platform == null) {
      return "jse";
    } else {
      return platform.toLowerCase();
    }
  }
  
  public void store() {
    DepotType depotType = new DepotType();
    Set<Entry<ArtifactKey,String>>
     es = artifactKeysToArtifacts_.entrySet();
    
    List<ArtifactType> artifacts = depotType.getArtifact();
    
    
    for (Entry<ArtifactKey,String> e: es) {
      ArtifactKey artPlatform = e.getKey();
      String file = e.getValue();
      String project = artPlatform.getProjectName();
      String platform = artPlatform.getPlatformName();
      String type = artPlatform.getArtifactType();
      
      ArtifactType art = new ArtifactType();
      File fullFile = files_.instance(file);
      String shortFile = fullFile.getName();
      
      art.setFilename(shortFile);
      art.setProject(project);
      platform = platform.toUpperCase();
      art.setPlatform(platform);
      art.setType(type);
      artifacts.add(art);
    }
    try {
      xmlFiles_.writeDepot_v1_0(dir_ + files_.getNameSeparator() + "depot.xml", depotType);
    } catch (Exception x) {
        throw new RuntimeException(x);
    }
  }

  @Override
  public String get(String projectName, String artifactType, String platformName) {
    String platform = getPlatformLower(platformName);
    String file = artifactKeysToArtifacts_.get(new ArtifactKey(projectName, artifactType, platform));
    if (file == null) {
      return null;
    }
    return file;
  }

  @Override
  public String getDir() {
    return dir_;
  }

  @Override
  public boolean has(String projectName) {
    Set<ArtifactKey> ks = artifactKeysToArtifacts_.keySet();
    Set<String> projects = new HashSet<String>();
    for (ArtifactKey ak: ks) {
      projects.add(ak.getProjectName());
    }
    return projects.contains(projectName);
  }
}
