package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class deals with a single dependency,
 * downloads it, checks the md5 and extracts
 * it in the local repository.   
 * @author scott
 *
 */
public class DependencyManager {
  private final I_FabLog log_;
  private final I_FabricateConstants constants_;
  private final I_FabFileIO files_;
  private final List<String> repositories_ = new ArrayList<String>();
  private I_RepositoryPathBuilder pathBuilder_;
  private String depOnDisk_;
  private String md5OnDisk_;
  private String extractOnDisk_;
  private String localRepository_;
  private boolean confirmIntegrity_;
  
  public DependencyManager(I_FabSystem sys, Collection<String> repos,
      I_RepositoryPathBuilder pathBuilder) {
    log_ = sys.getLog();
    constants_ = sys.getConstants();
    files_ = sys.getFileIO();
    repositories_.addAll(repos);
    pathBuilder_ = pathBuilder;
  }
  
  
  public void manange(I_Dependency dep ) {
    depOnDisk_ = null;
    md5OnDisk_ = null;
    extractOnDisk_ = null;
   
    if (find(dep)) {
      //it all on disk
      if (confirmIntegrity_) {
        if (!checkMd5AndExtract(dep)) {
          downloadEtc(dep);
        }
      }
    } else {
      downloadEtc(dep);
    }
  }
  
  /**
   * Checks if the .md5 files contents
   * match the md5 of the artifact, and 
   * deletes the files if they don't match
   * @return
   */
  private boolean checkMd5AndExtract(I_Dependency dep) {
    
    return false;
  }


  private boolean downloadArtifact() {
   
    return false;
  }
  
  private boolean downloadMd5() {
    
    return false;
  }
  
  private void downloadEtc(I_Dependency dep) {
    for (String remoteRepo: repositories_) {
      if (downloadMd5()) {
        if (downloadArtifact()) {
          if (dep.isExtract()) {
            extract();
          }
        }
      }
    }
    
  }
  
  private void extract() {
    
  }
  /**
   * identifies the variables
   * depOnDisk_
   * md5OnDisk_
   * extractOnDisk_
   * @param 
   * @return true if the artifact and md5 are 
   *   on disk;
   */
  private boolean find(I_Dependency dep) {
    
    return false;
  }


  public String getLocalRepository() {
    return localRepository_;
  }


  public void setLocalRepository(String localRepository) {
    this.localRepository_ = localRepository;
  }


  public boolean isConfirmIntegrity() {
    return confirmIntegrity_;
  }


  public void setConfirmIntegrity(boolean confirmIntegrity) {
    this.confirmIntegrity_ = confirmIntegrity;
  }
  
  
}
