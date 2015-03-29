package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.FabLog;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is a thread safe class that downloads
 * files from maven remote_repositories and puts them 
 * in the local_repository
 * @author scott
 *
 */
public class RepositoryManager implements I_RepositoryManager {
  private List<String> repositories_ = new ArrayList<String>();

  private final I_FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_Fabricate fab_;
  private final ConcurrentLinkedQueue<I_Dependency> dependencies_;
  private final I_FabricateConstants constants_;
  private final I_SystemMessages messages_;
  private final I_RepositoryFactory factory_;
  private IOException localException_;
  private DependencyNotAvailableException remoteException_;
  
  public RepositoryManager(I_FabSystem sys, I_Fabricate fab, I_RepositoryFactory factory) {
    sys_ = sys;
    constants_ = sys.getConstants();
    messages_ = constants_.getSystemMessages();
    log_ = sys.getLog();
    files_ = sys.getFileIO();
    fab_ = fab;
    setRepositories(fab_.getRemoteRepositories());
    factory_ = factory;
    dependencies_ = sys_.newConcurrentLinkedQueue(I_Dependency.class);
  }
  
  

  

  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_RepositoryManager#addDependencies(java.util.concurrent.ConcurrentLinkedQueue)
   */
  @Override
  public void addDependencies(Collection<I_Dependency> dependencies) {
    dependencies_.addAll(dependencies);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_RepositoryManager#checkRepositories()
   */
  @Override
  public void checkRepositories() {
    Iterator<String> repos = repositories_.iterator();
    while (repos.hasNext()) {
      String repo = repos.next();
      final String repoUrl = repo;
      try {
        int status = files_.check(repoUrl);
        if (status >= 300) {
          repos.remove();
        }
      } catch (IOException e) {
        I_SystemMessages messages =  constants_.getSystemMessages();
        String line = messages.getTheFollowingRemoteRepositoryAppearsToBeDown();
        log_.println(RepositoryManager.class.getSimpleName() + " " + line + sys_.lineSeparator() + repo + sys_.lineSeparator());
      }
    }
    if (repositories_.size() == 0) {
      I_SystemMessages messages =  constants_.getSystemMessages();
      throw new RuntimeException(messages.getNoRemoteRepositoriesCouldBeReached());
    }
  }

  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_RepositoryManager#getLocalException()
   */
  @Override
  public IOException getLocalException() {
    return localException_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_RepositoryManager#getRemoteException()
   */
  @Override
  public DependencyNotAvailableException getRemoteException() {
    return remoteException_;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_RepositoryManager#getDependencies()
   */
  @Override
  public ConcurrentLinkedQueue<I_Dependency> getDependencies() {
    return dependencies_;
  }


  public void logError(DependencyNotAvailableException x) {
    I_Dependency dep = x.getDependency();
    StringBuilder sb = new StringBuilder();
    I_FabricateConstants constants = sys_.getConstants();
    I_SystemMessages messages = constants.getSystemMessages();
   
    sb.append(messages.getThereWasAProblemVerifyingOrDownloadingTheFollowingDependency());
    sb.append(sys_.lineSeparator());
    String artifact = dep.getArtifact();
    if (artifact == null) {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getArtifactColon(), artifact));
    } else {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getArtifactColon(), "'", artifact, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    
    String file = dep.getFileName();
    if (file == null) {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getFileNameColon(), file));
    } else {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getFileNameColon(), "'", file, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    String group = dep.getGroup();
    if (group == null) {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getGroupColon(), group));
    } else {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getGroupColon(), "'", group, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    String type = dep.getType();
    if (type == null) {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getTypeColon(), type));
    } else {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getTypeColon(), "'", type, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    String version = dep.getVersion();
    if (version == null) {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getVersionColon(), version));
    } else {
      sb.append(FabLog.orderLine(constants.isLeftToRight(), "\t", messages.getVersionColon(), "'", version, "'"));
    }
    sb.append(sys_.lineSeparator());
    sb.append(messages.getForSemicolon());
    sb.append(sys_.lineSeparator());
    String project = dep.getProject();
    if (project == null) {
      sb.append(fab_.getFabricateXmlRunDir() + "fabricate.xml");
      sb.append(sys_.lineSeparator());
    } else {
      sb.append(fab_.getProjectsDir() + project);
      sb.append(sys_.lineSeparator());
    }
      
    I_FabLog log = sys_.getLog();
    log.printTrace(x);
    log.println(sb.toString());
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_RepositoryManager#manageDependencies()
   */
  @Override
  public boolean manageDependencies() {
    if (log_.isLogEnabled(RepositoryManager.class)) {
      log_.println(RepositoryManager.class.getSimpleName() + " " + messages_.getManagingTheFollowingLocalRepository() + sys_.lineSeparator() +
          fab_.getFabricateRepository() + sys_.lineSeparator());
    }
    checkRepositories();
    if (log_.isLogEnabled(RepositoryManager.class)) {
      StringBuilder sb = new StringBuilder();
      sb.append(messages_.getUsingTheFollowingRemoteRepositories());
      for (String repo: repositories_) {
        sb.append(sys_.lineSeparator());
        sb.append(repo);
      }
      log_.println(RepositoryManager.class.getSimpleName() + " " + sb.toString()  + sys_.lineSeparator());
    }
    String localRepo = fab_.getFabricateRepository();
    if (!files_.exists(localRepo)) {
      files_.mkdirs(localRepo);
    }
    String repoLockFile = localRepo + ".modifying";
    if (files_.exists(repoLockFile)) {
      log_.println(CommandLineArgs.END);
      throw new RuntimeException(messages_.getTheFollowingLocalRepositoryIsLockedByAnotherProcess() +
          sys_.lineSeparator() + localRepo);
    }
    try {
      files_.create(repoLockFile);
    } catch (IOException e1) {
      log_.println(CommandLineArgs.END);
      throw new RuntimeException(e1);
    }
    files_.deleteOnExit(repoLockFile);
    int threads = fab_.getThreads();
    List<I_Dependency> fds = fab_.getDependencies();
    dependencies_.addAll(fds);
    
    if (dependencies_.size() >= 1) {
      I_SystemMessages messages = constants_.getSystemMessages();
      if (log_.isLogEnabled(RepositoryManager.class)) {
        log_.println(RepositoryManager.class.getSimpleName() + " " + messages.getCheckingFabricateRuntimeDependencies());
      }
      
      if (threads <= 1) {
        I_DependenciesManager dm = factory_.createDependenciesManager(sys_, dependencies_);
        dm.setOnMainThread(true);
        dm.setRemoteRepositories(repositories_);
        dm.run();
      } else {
        ExecutorService es = Executors.newFixedThreadPool(threads);
        Set<I_DependenciesManager> dms = new HashSet<I_DependenciesManager>();
        for (int i = 0; i < threads; i++) {
          I_DependenciesManager dm = factory_.createDependenciesManager(sys_, dependencies_);
          dm.setOnMainThread(true);
          dm.setRemoteRepositories(repositories_);
          dm.setFabricate(fab_);
          dms.add(dm);
          
          es.execute(dm);
        }
        boolean allDone = false;
        while (!allDone) {
          boolean wait = false;
          for (I_DependenciesManager dm: dms) {
            if (!dm.isFinished()) {
              dm.waitUntilFinished();
              wait = true;
            }
          }
          if (!wait) {
            allDone = true;
          }
        }
        for (I_DependenciesManager dm: dms) {
          localException_ = dm.getLocalException();
          if (localException_ != null) {
            break;
          }
          remoteException_ = dm.getRemoteException();
          if (remoteException_ != null) {
            break;
          }
        }
        es.shutdownNow();
      }
    }
    try {
      files_.delete(repoLockFile);
    } catch (IOException e) {
      log_.println(CommandLineArgs.END);
      throw new RuntimeException(e);
    }
    return remoteException_ == null;
  }
  
  private void setRepositories(List<String> repositories ) {
    if (repositories != null) {
      for (String repo: repositories) {
        repositories_.add(repo);
      }
    }
  }

}
