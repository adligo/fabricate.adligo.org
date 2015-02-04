package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * This class deals with a single dependency,
 * downloads it, checks the md5 and extracts
 * it in the local repository.   
 * @author scott
 *
 */
public class DependencyManager {
  private final I_FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabricateConstants constants_;
  private final I_FabFileIO files_;
  private final List<String> repositories_ = new ArrayList<String>();
  private I_RepositoryPathBuilder pathBuilder_;
  private I_RepositoryPathBuilderFactory pathBuilderFactory_;
  
  private String depOnDisk_;
  private String md5OnDisk_;
  private String extractOnDisk_;
  private String localRepository_;
  private boolean confirmIntegrity_;
  private boolean downloadedAnyArtifact_ = false;
  private IOException lastIOException_;
  
  public DependencyManager(I_FabSystem sys, Collection<String> repos,
      I_RepositoryPathBuilder pathBuilder) {
    sys_ = sys;
    log_ = sys.getLog();
    constants_ = sys.getConstants();
    files_ = sys.getFileIO();
    repositories_.addAll(repos);
    pathBuilder_ = pathBuilder;
  }
  
  
  public boolean manange(I_Dependency dep ) {
    reset();
   
    if (find(dep)) {
      //it all on disk
      if (confirmIntegrity_) {
        if (!checkMd5AndExtract(dep)) {
          cleanFiles();
          if (downloadEtc(dep)) {
            return true;
          }
        }
      }
    } else {
      if (downloadEtc(dep)) {
        return true;
      }
    }
    return false;
  }

  
  /**
   * Checks if the .md5 files contents
   * match the md5 of the artifact, and 
   * deletes the files if they don't match
   * @return
   */
  private boolean checkMd5AndExtract(I_Dependency dep) {
    if (checkMd5s()) {
      if (dep.isExtract()) {
        if (checkExtract(dep)) {
          return true;
        }
      } else {
        return true;
      }
    }
    return false;
  }


  private boolean checkMd5s() {
    String md5FromFile = null;
    I_SystemMessages messages = constants_.getSystemMessages();
    
    try {
      md5FromFile = files_.readFile(md5OnDisk_);
    } catch (IOException x) {
      logMd5Mismatch(messages);
      lastIOException_ = x;
      return false;
    }
    String md5 = null;
    try {
       md5 = files_.calculateMd5(depOnDisk_);
    } catch (IOException x) {
      logMd5Mismatch(messages);
      lastIOException_ = x;
      return false;
    }
    if ( !md5FromFile.equals(md5)) {
      logMd5Mismatch(messages);
      return false;
    }
    log_.println(
        messages.getTheFollowingArtifact() + sys_.lineSeperator() +
        depOnDisk_ + sys_.lineSeperator() +
        messages.getPassedTheMd5Check());
    return true;
  }


  public void logMd5Mismatch(I_SystemMessages messages) {
    log_.println(
        messages.getTheFollowingArtifact() + sys_.lineSeperator() +
        depOnDisk_ + sys_.lineSeperator() +
        messages.getDidNotPassTheMd5Check());
  }


  /**
   * may try to re-extract the files from the zip.
   * @param dep
   * @return
   */
  private boolean checkExtract(I_Dependency dep) {
    if (dep.isExtract()) {
      ZipFile zipFile;
      I_SystemMessages messages = constants_.getSystemMessages();
      
      try {
        zipFile = files_.newZipFile(depOnDisk_);
      } catch (IOException e) {
        logExtractFailure(messages);
        lastIOException_ = e;
        return false;
      }
      if (files_.verifyZipFileExtract(extractOnDisk_, zipFile)) {
        logExtractVerificationSuccess(messages);
      } else {
        logExtractFailure(messages);
      }
    }
    return true;
  }


  private boolean downloadArtifact(I_RepositoryPathBuilder remotePathBuilder, I_Dependency dep) {
    String from = remotePathBuilder.getArtifactUrl(dep);
    String to = pathBuilder_.getArtifactPath(dep);
    I_SystemMessages messages = constants_.getSystemMessages();
    log_.println(
        messages.getStartingDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
        from + sys_.lineSeperator() +
        messages.getToTheFollowingFolder() + sys_.lineSeperator() +
        to);
    try {
      files_.downloadFile(from, to);
    } catch (IOException x) {
      log_.println(
          messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
          from + sys_.lineSeperator() +
          messages.getFailed());
      lastIOException_ = x;
      return false;
    }
    if (!files_.exists(to)) {
      log_.println(
          messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
          from + sys_.lineSeperator() +
          messages.getFailed());
      return false;
    }
    downloadedAnyArtifact_ = true;
    log_.println(
        messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
        from + sys_.lineSeperator() +
        messages.getFinished());
    return true;
  }
  
  private boolean downloadMd5(I_RepositoryPathBuilder remotePathBuilder, I_Dependency dep) {
    String from = remotePathBuilder.getMd5Url(dep);
    String to = pathBuilder_.getMd5Path(dep);
    I_SystemMessages messages = constants_.getSystemMessages();
    log_.println(
        messages.getStartingDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
        from + sys_.lineSeperator() +
        messages.getToTheFollowingFolder() + sys_.lineSeperator() +
        to);
    try {
      files_.downloadFile(from, to);
    } catch (IOException x) {
      log_.println(
          messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
          from + sys_.lineSeperator() +
          messages.getFailed());
      lastIOException_ = x;
      return false;
    }
    if (!files_.exists(to)) {
      log_.println(
          messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
          from + sys_.lineSeperator() +
          messages.getFailed());
      return false;
    }
    log_.println(
        messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeperator() +
        from + sys_.lineSeperator() +
        messages.getFinished());
    return true;
  }
  
  private boolean downloadEtc(I_Dependency dep) {
    for (String remoteRepo: repositories_) {
      I_RepositoryPathBuilder builder = pathBuilderFactory_.create(remoteRepo);
      if (downloadMd5(builder, dep)) {
        if (downloadArtifact(builder, dep)) {
          if (checkMd5s()) {
            if (dep.isExtract()) {
              if (extract(dep)) {
                if (checkExtract(dep)) {
                  return true;
                }
              }
              cleanExtract();
              return false;
            } else {
              return true;
            }
          } else {
            cleanFiles();
          }
        }
      }
    }
    return false;
  }
  
  /**
   * calls should check isExtract() first!
   * @param dep
   * @return
   */
  private boolean extract(I_Dependency dep) {
    cleanExtract();
    I_SystemMessages messages = constants_.getSystemMessages();
    log_.println(
        messages.getExtractingTheFollowingArtifact() + sys_.lineSeperator() +
        depOnDisk_);
    try {
      files_.unzip(depOnDisk_, extractOnDisk_);
    } catch (IOException x) {
      logExtractFailure(messages);
      lastIOException_ = x;
      return false;
    }
    ZipFile zipFile = null;
    try {
      zipFile = files_.newZipFile(depOnDisk_);
    } catch (IOException e) {
      logExtractFailure(messages);
      lastIOException_ = e;
      return false;
    }
    if (!files_.verifyZipFileExtract(extractOnDisk_, zipFile)) {
      logExtractFailure(messages);
      return false;
    }
    logExtractVerificationSuccess(messages);
    return true;
  }


  public void logExtractVerificationSuccess(I_SystemMessages messages) {
    log_.println(
        messages.getTheFollowingArtifact() + sys_.lineSeperator() +
        depOnDisk_ + sys_.lineSeperator() +
        messages.getPassedTheExtractCheck());
  }


  public void logExtractFailure(I_SystemMessages messages) {
    log_.println(
        messages.getExtractionOfTheFollowingArtifact() + sys_.lineSeperator() +
        depOnDisk_ + sys_.lineSeperator() + 
        messages.getFailed());
  }
  /**
   * identifies the variables
   * depOnDisk_
   * md5OnDisk_
   * extractOnDisk_
   * @param 
   * @return true if the artifact, md5 and extract are 
   *   all on disk;
   */
  private boolean find(I_Dependency dep) {
    depOnDisk_ = pathBuilder_.getArtifactPath(dep);
    md5OnDisk_ = pathBuilder_.getMd5Path(dep);
    if (dep.isExtract()) {
      extractOnDisk_ = pathBuilder_.getExtractPath(dep, constants_);
      if (!files_.exists(extractOnDisk_)) {
        if (!extract(dep)) {
          return false;
        }
      }
    }
    if (!files_.exists(depOnDisk_)) {
      return false;
    }
    
    if (!files_.exists(md5OnDisk_)) {
      return false;
    }
    
    return true;
  }

  private void reset() {
    depOnDisk_ = null;
    md5OnDisk_ = null;
    extractOnDisk_ = null;
    downloadedAnyArtifact_ = false;
  }

  private void cleanFiles() {
    if (files_.exists(depOnDisk_)) {
      try {
        files_.delete(depOnDisk_);
      } catch (IOException x) {
        lastIOException_ = x;
      }
    }
    if (files_.exists(md5OnDisk_)) {
      try {
        files_.delete(md5OnDisk_);
      } catch (IOException x) {
        lastIOException_ = x;
      }
    }
    cleanExtract();
  }


  private void cleanExtract() {
    if (files_.exists(extractOnDisk_)) {
      try {
        files_.deleteRecursive(extractOnDisk_);
      } catch (IOException x) {
        lastIOException_ = x;
      }
    }
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


  public I_RepositoryPathBuilderFactory getPathBuilderFactory() {
    return pathBuilderFactory_;
  }


  public void setPathBuilderFactory(I_RepositoryPathBuilderFactory pathBuilderFactory) {
    this.pathBuilderFactory_ = pathBuilderFactory;
  }


  public boolean isDownloadedAnyArtifact() {
    return downloadedAnyArtifact_;
  }


  public IOException getLastIOException() {
    return lastIOException_;
  }
}
