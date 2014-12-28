package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.depot.v1_0.ArtifactType;
import org.adligo.fabricate.xml.io.depot.v1_0.DepotType;
import org.adligo.fabricate.xml_io.DepotIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
  private String dir_;
  private I_FabContext ctx_;
  
  /**
   * This is the main runtime index of what is in the 
   * depot directory, it is generally created
   * at instance construction time, and updated
   * as new things are added to the depot directory
   */
  private ConcurrentHashMap<String,ConcurrentHashMap<String,String>> 
      artifactTypesToProjectsToArtifacts_ = 
      new ConcurrentHashMap<String, ConcurrentHashMap<String,String>>();
  private ConcurrentHashMap<String,ArtifactType> projectsToArtifacts_ = 
      new ConcurrentHashMap<String, ArtifactType>();
  
  public Depot(String dir, I_FabContext ctx, DepotType depot) {
    dir_ = dir;
    ctx_ = ctx;
    List<ArtifactType> artifacts = depot.getArtifact();
    for (ArtifactType art: artifacts) {
      String type = art.getType();
      ConcurrentHashMap<String,String> typeMap =  artifactTypesToProjectsToArtifacts_.get(type);
      if (typeMap == null) {
        typeMap = new ConcurrentHashMap<String, String>();
        artifactTypesToProjectsToArtifacts_.putIfAbsent(type, typeMap);
        typeMap = artifactTypesToProjectsToArtifacts_.get(type);
      }
      String project = art.getProject();
      String file = art.getFilename();
      //the stored file is relative to the depot
      typeMap.put(project, dir + File.separator + file);
      
    }
  }

  @Override
  public boolean add(String externalFile, I_DepotEntry entryData) {
    String projectName = entryData.getProjectName();
    if (StringUtils.isEmpty(projectName)) {
      throw new IllegalArgumentException("No projectName!");
    }
    String artifactType = entryData.getArtifactType();
    if (StringUtils.isEmpty(artifactType)) {
      throw new IllegalArgumentException("No artifactType!");
    }
    File file = new File(externalFile);
    if (!file.exists()) {
      throw new IllegalArgumentException("No externalFile!");
    }
    String fileName = file.getName();
    String artDir = dir_ + File.separator + artifactType + "s";
    File artFile = new File(artDir);
    if (!artFile.exists()) {
      if (!new File(artDir).mkdirs()) {
        throw new IllegalStateException("problem creating " + artDir);
      }
    }
    String depotFileName = artDir + File.separator + fileName;
    
    File depotFile = new File(depotFileName);
    if (depotFile.exists()) {
      throw new IllegalStateException("The following file already exists in the depot." + System.lineSeparator() +
          "\t" + depotFileName);
    }
    File out = new File(depotFileName);
    if (ctx_.isLogEnabled(Depot.class)) {
      ThreadLocalPrintStream.println("Moving file " + externalFile + System.lineSeparator() +
            "\tto " + depotFileName + System.lineSeparator());
    }
    try {
      Files.move(file.toPath(), out.toPath(), StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    } 
    //ok the file is in
    ConcurrentHashMap<String, String> subMap = artifactTypesToProjectsToArtifacts_.get(artifactType);
    if (subMap == null) {
      artifactTypesToProjectsToArtifacts_.putIfAbsent(artifactType, new ConcurrentHashMap<String, String>());
      subMap = artifactTypesToProjectsToArtifacts_.get(artifactType);
    }
    subMap.put(projectName, depotFileName);
    if (ctx_.isLogEnabled(Depot.class)) {
      ThreadLocalPrintStream.println("Depot now has " + artifactType + "/" + projectName + "/" + depotFileName);
    }
    return true;
  }

  public void store() {
    DepotType type = new DepotType();
    Set<Entry<String,ConcurrentHashMap<String, String>>>
     es = artifactTypesToProjectsToArtifacts_.entrySet();
    
    List<ArtifactType> artifacts = type.getArtifact();
    
    int depotDir = dir_.length() + 1;
    
    for (Entry<String,ConcurrentHashMap<String, String>> e: es) {
      String artType = e.getKey();
      ConcurrentHashMap<String, String> projToFile = e.getValue();
      Set<Entry<String,String>> pfes = projToFile.entrySet();
      for (Entry<String,String> pfe: pfes) {
        String project = pfe.getKey();
        String fileAbs = pfe.getValue();
        String relFile = fileAbs.substring(depotDir, fileAbs.length());
        ArtifactType art = new ArtifactType();
        art.setFilename(relFile);
        art.setProject(project);
        art.setType(artType);
        artifacts.add(art);
      }
    }
    try {
      DepotIO.write(new File(dir_ + File.separator + "depot.xml"), type);
    } catch (Exception x) {
        throw new RuntimeException(x);
    }
  }

  @Override
  public String get(String projectName) {
    return get(projectName, "jar");
  }

  @Override
  public String get(String projectName, String artifactType) {
    ConcurrentHashMap<String, String> subMap = artifactTypesToProjectsToArtifacts_.get(artifactType);
    if (subMap == null) {
      return null;
    }
    String file =  subMap.get(projectName);
    if (!new File(file).exists()) {
      throw new IllegalStateException("The following file doesn't appear to be on disk" + System.lineSeparator() +
          "\t" + file);
    }
    return file;
  }

  @Override
  public String getDir() {
    return dir_;
  }
}
