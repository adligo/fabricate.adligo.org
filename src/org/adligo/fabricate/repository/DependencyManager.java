package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

/**
 * This class deals with a single dependency,
 * downloads it, checks the md5 and extracts
 * it in the local repository.    It is NOT thread safe.
 *  
 * @author scott
 *
 */
public class DependencyManager implements I_DependencyManager {
  private final I_FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabricateConstants constants_;
  private final I_FabFileIO files_;
  /**
   * keeps a local copy
   * so that concurrent iteration
   * is not a issue.
   */
  private final List<String> repositories_ = new ArrayList<String>();
  private I_RepositoryPathBuilder pathBuilder_;
  private I_RepositoryFactory factory_;
  
  private String depOnDisk_;
  private String md5OnDisk_;
  private String extractOnDisk_;
  private String localRepository_;
  private boolean confirmIntegrity_;
  private boolean downloadedAnyArtifact_ = false;
  private Map<String,WeightedException> reposToExceptions_ = new HashMap<String,WeightedException>();
  
  
  public DependencyManager(I_FabSystem sys, Collection<String> repos,
      I_RepositoryPathBuilder pathBuilder) {
    sys_ = sys;
    log_ = sys.getLog();
    constants_ = sys.getConstants();
    files_ = sys.getFileIO();
    repositories_.addAll(repos);
    pathBuilder_ = pathBuilder;
  }
  
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependencyManager#manange(org.adligo.fabricate.models.dependencies.I_Dependency)
   */
  @Override
  public void manange(I_Dependency dep ) throws DependencyNotAvailableException, IOException {
    reset();
   
    if (find(dep)) {
      //it all on disk
      if (confirmIntegrity_) {
        if (!checkMd5AndExtract(dep)) {
          cleanFiles();
          if (!downloadEtc(dep)) {
            throwDependencyNotAvailable(dep);
          }
        }
      }
    } else {
      if (!downloadEtc(dep)) {
        throwDependencyNotAvailable(dep);
      }
    }
  }


 

  
  /**
   * Checks if the .md5 files contents
   * match the md5 of the artifact, and 
   * deletes the files if they don't match
   * @return
   */
  private boolean checkMd5AndExtract(I_Dependency dep) {
    if (checkMd5s(null)) {
      if (dep.isExtract()) {
        if (checkExtract(null, dep)) {
          return true;
        }
      } else {
        return true;
      }
    }
    return false;
  }


  private boolean checkMd5s(String remoteRepository) {
    String md5FromFile = null;
    I_SystemMessages messages = constants_.getSystemMessages();
    
    try {
      md5FromFile = files_.readFile(md5OnDisk_);
      md5FromFile = md5FromFile.replaceAll(sys_.lineSeparator(), "");
    } catch (IOException x) {
      if (remoteRepository != null) {
        reposToExceptions_.put(remoteRepository, new WeightedException(x, 2));
      }
      return false;
    }
    String md5 = null;
    try {
       md5 = files_.calculateMd5(depOnDisk_);
    } catch (IOException x) {
      if (remoteRepository != null) {
        reposToExceptions_.put(remoteRepository, new WeightedException(x, 3));
      }
      return false;
    }
    if ( !md5FromFile.equals(md5)) {
      String message = getMd5MismatchMessage(messages, md5FromFile, md5);
      if (log_.isLogEnabled(DependencyManager.class)) {
        log_.println(message);
      }
      reposToExceptions_.put(remoteRepository, new WeightedException(
          new IllegalStateException(message), 4));
      return false;
    }
    if (log_.isLogEnabled(DependencyManager.class)) {
      log_.println(
          messages.getTheFollowingArtifact() + sys_.lineSeparator() +
          depOnDisk_ + sys_.lineSeparator() +
          messages.getPassedTheMd5Check() + sys_.lineSeparator());
    }
    return true;
  }


  public String getMd5MismatchMessage(I_SystemMessages messages, String fileMd5, String md5) {
    return 
          messages.getTheFollowingArtifact() + sys_.lineSeparator() +
          depOnDisk_ + sys_.lineSeparator() +
          messages.getDidNotPassTheMd5Check() + sys_.lineSeparator() + 
          fileMd5 + sys_.lineSeparator() + 
          md5;
  }


  /**
   * may try to re-extract the files from the zip.
   * @param dep
   * @return
   */
  private boolean checkExtract(String remoteRepository, I_Dependency dep) {
    if (dep.isExtract()) {
      ZipFile zipFile;
      I_SystemMessages messages = constants_.getSystemMessages();
      
      try {
        zipFile = files_.newZipFile(depOnDisk_);
      } catch (IOException e) {
        String message = getExtractFailure(messages);
        if (log_.isLogEnabled(DependencyManager.class)) {
          log_.println(message);
        }
        if (remoteRepository != null) {
          reposToExceptions_.put(remoteRepository, new WeightedException(
              new IllegalStateException(message), 4));
        }
        return false;
      }
      if (files_.verifyZipFileExtract(extractOnDisk_, zipFile)) {
        logExtractVerificationSuccess(messages);
      } else {
        String message = getExtractFailure(messages);
        if (log_.isLogEnabled(DependencyManager.class)) {
          log_.println(message);
        }
        return false;
      }
    }
    return true;
  }


  private boolean downloadArtifact(String remoteRepository, I_RepositoryPathBuilder remotePathBuilder, I_Dependency dep) {
    String from = remotePathBuilder.getArtifactUrl(dep);
    String to = pathBuilder_.getArtifactPath(dep);
    I_SystemMessages messages = constants_.getSystemMessages();
    
    try {
      files_.downloadFile(from, to);
    } catch (IOException x) {
      if (log_.isLogEnabled(DependencyManager.class)) {
        log_.println(
            messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeparator() +
            from + sys_.lineSeparator() +
            messages.getFailed() + sys_.lineSeparator());
        log_.printTrace(x);
      
      }
      reposToExceptions_.put(remoteRepository, new WeightedException(x, 1));
      return false;
    }
    if (!files_.exists(to)) {
      if (log_.isLogEnabled(DependencyManager.class)) {
        log_.println(
            messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeparator() +
            from + sys_.lineSeparator() +
            messages.getFailed() + sys_.lineSeparator());
      }
      return false;
    }
    downloadedAnyArtifact_ = true;
    return true;
  }
  
  private boolean downloadMd5(String repository, I_RepositoryPathBuilder remotePathBuilder, I_Dependency dep) throws DependencyNotAvailableException {
    String from = remotePathBuilder.getMd5Url(dep);
    String to = pathBuilder_.getMd5Path(dep);
    String folder = pathBuilder_.getFolderPath(dep);
    
    if (!files_.exists(folder)) {
      if (!files_.mkdirs(folder)) {
        //it may be created by another thread :)_
        if (!files_.exists(folder)) {
          if (log_.isLogEnabled(DependencyManager.class)) {
            I_FileMessages messages = constants_.getFileMessages();
            throw new DependencyNotAvailableException(dep, messages.getThereWasAProblemCreatingTheFollowingDirectory() + sys_.lineSeparator() +
                folder);
          }
        }
      }
    }
    I_SystemMessages messages = constants_.getSystemMessages();
   
    try {
      files_.downloadFile(from, to);
    } catch (IOException x) {
      if (log_.isLogEnabled(DependencyManager.class)) {
        log_.printTrace(x);
        log_.println(
            messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeparator() +
            from + sys_.lineSeparator() +
            messages.getFailed() + sys_.lineSeparator());
      }
      reposToExceptions_.put(repository, new WeightedException(x, 0));
      return false;
    }
    if (!files_.exists(to)) {
      if (log_.isLogEnabled(DependencyManager.class)) {
        log_.println(
            messages.getTheDownloadFromTheFollowingUrl() + sys_.lineSeparator() +
            from + sys_.lineSeparator() +
            messages.getFailed() + sys_.lineSeparator());
      }
      return false;
    }
    return true;
  }
  
  private boolean downloadEtc(I_Dependency dep) throws DependencyNotAvailableException, IOException {
    for (String remoteRepo: repositories_) {
      I_RepositoryPathBuilder builder = factory_.createRepositoryPathBuilder(remoteRepo);
      if (log_.isLogEnabled(DependencyManager.class)) {
        log_.println("trying to obtain dependency " + sys_.lineSeparator() +
            dep + sys_.lineSeparator() +
            "from" + sys_.lineSeparator() +
            remoteRepo);
      }
      if (downloadMd5(remoteRepo, builder, dep)) {
        if (downloadArtifact(remoteRepo, builder, dep)) {
          if (checkMd5s(remoteRepo)) {
            if (dep.isExtract()) {
              if (extract(remoteRepo, dep)) {
                if (checkExtract(remoteRepo,dep)) {
                  return true;
                }
              }
              cleanExtract();
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
  private boolean extract(String remoteRepository, I_Dependency dep) throws IOException {
    cleanExtract();
    I_SystemMessages messages = constants_.getSystemMessages();
    if (log_.isLogEnabled(DependencyManager.class)) {
      log_.println(
          messages.getExtractingTheFollowingArtifact() + sys_.lineSeparator() +
          depOnDisk_ + sys_.lineSeparator());
    }
    files_.unzip(depOnDisk_, extractOnDisk_);
    
    ZipFile zipFile = null;
    zipFile = files_.newZipFile(depOnDisk_);
    
    if (!files_.verifyZipFileExtract(extractOnDisk_, zipFile)) {
      String message = getExtractFailure(messages);
      reposToExceptions_.put(remoteRepository, new WeightedException(
          new IOException(message), 11));
      return false;
    }
    return true;
  }


  public void logExtractVerificationSuccess(I_SystemMessages messages) {
    if (log_.isLogEnabled(DependencyManager.class)) {
      log_.println(
          messages.getTheFollowingArtifact() + sys_.lineSeparator() +
          depOnDisk_ + sys_.lineSeparator() +
          messages.getPassedTheExtractCheck() + sys_.lineSeparator());
    }
  }

  public String getExtractFailure(I_SystemMessages messages) {
    return 
          messages.getExtractionOfTheFollowingArtifact() + sys_.lineSeparator() +
          depOnDisk_ + sys_.lineSeparator() + 
          messages.getFailed() + sys_.lineSeparator();
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
        return false;
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

  private void cleanFiles() throws IOException {
    if (files_.exists(depOnDisk_)) {
     files_.delete(depOnDisk_);
    }
    if (files_.exists(md5OnDisk_)) {
      files_.delete(md5OnDisk_);
    }
    cleanExtract();
  }


  private void cleanExtract() throws IOException {
    if (extractOnDisk_ != null) {
      if (files_.exists(extractOnDisk_)) {
        files_.deleteRecursive(extractOnDisk_);
      }
    }
  }
  
  public String getLocalRepository() {
    return localRepository_;
  }


  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependencyManager#setLocalRepository(java.lang.String)
   */
  @Override
  public void setLocalRepository(String localRepository) {
    this.localRepository_ = localRepository;
  }


  public boolean isConfirmIntegrity() {
    return confirmIntegrity_;
  }


  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependencyManager#setConfirmIntegrity(boolean)
   */
  @Override
  public void setConfirmIntegrity(boolean confirmIntegrity) {
    this.confirmIntegrity_ = confirmIntegrity;
  }


  public I_RepositoryFactory getPathBuilderFactory() {
    return factory_;
  }


  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependencyManager#setPathBuilderFactory(org.adligo.fabricate.repository.I_RepositoryFactory)
   */
  @Override
  public void setFactory(I_RepositoryFactory pathBuilderFactory) {
    this.factory_ = pathBuilderFactory;
  }


  public boolean isDownloadedAnyArtifact() {
    return downloadedAnyArtifact_;
  }

  
  private void throwDependencyNotAvailable(I_Dependency dep) throws DependencyNotAvailableException {
    if (log_.isLogEnabled(DependencyManager.class)) {
      log_.println("throwing " + DependencyNotAvailableException.class.getName());
    }
    Collection<WeightedException> wes = reposToExceptions_.values();
    WeightedException biggestWeight = null;
    for (WeightedException we: wes) {
      if (biggestWeight == null) {
        biggestWeight = we;
      } else {
        if (we.getWeight() > biggestWeight.getWeight()) {
          biggestWeight = we;
        }
      }
    }
    Exception x = biggestWeight.getException();
    throw new DependencyNotAvailableException(dep, x.getMessage(), x);
  }
}
