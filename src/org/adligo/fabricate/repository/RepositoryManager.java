package org.adligo.fabricate.repository;

import org.adligo.fabricate.FabricateFactory;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
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
public class RepositoryManager {
  private static PrintStream OUT = System.out;
  private List<String> repositories_ = new ArrayList<String>();

  private final I_FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_Fabricate fab_;
  private final I_FabricateConstants constants_;
  private final FabricateFactory factory_;
  
  public RepositoryManager(I_FabSystem sys, I_Fabricate fab, FabricateFactory factory) {
    sys_ = sys;
    log_ = sys.getLog();
    files_ = sys.getFileIO();
    fab_ = fab;
    setRepositories(fab_.getRemoteRepositories());
    factory_ = factory;
    constants_ = sys_.getConstants();
  }
  
  public void manageDependencies() {
    
    String localRepo = fab_.getFabricateRepository();
    if (!files_.exists(localRepo)) {
      files_.mkdirs(localRepo);
    }
    String repoLockFile = localRepo + files_.getNameSeparator() + ".modifying";
    if (files_.exists(repoLockFile)) {
      I_SystemMessages messages = constants_.getSystemMessages();
      log_.println(messages.getTheFollowingLocalRepositoryIsLockedByAnotherProcess() +
          sys_.lineSeperator() + localRepo);
      log_.println(CommandLineArgs.END);
      throw new RuntimeException();
    }
    files_.deleteOnExit(repoLockFile);
    int threads = fab_.getThreads();
    List<I_Dependency> fds = fab_.getDependencies();
    
    if (fds.size() >= 1) {
      I_SystemMessages messages = constants_.getSystemMessages();
      log_.println(messages.getCheckingFabricateRuntimeDependencies());
        
      ConcurrentLinkedQueue<I_Dependency> deps = new ConcurrentLinkedQueue<I_Dependency>();
      deps.addAll(fds);
      if (threads <= 1) {
        I_DependenciesManager dm = factory_.createDependenciesManager(sys_, deps);
        dm.setOnMainThread(true);
        dm.setRemoteRepositories(repositories_);
        dm.run();
      } else {
        ExecutorService es = Executors.newFixedThreadPool(threads);
        Set<I_DependenciesManager> dms = new HashSet<I_DependenciesManager>();
        for (int i = 0; i < threads; i++) {
          I_DependenciesManager dm = factory_.createDependenciesManager(sys_, deps);
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
              if (log_.isLogEnabled(RepositoryManager.class)) {
                log_.println("waiting on " + dm);
              }
              dm.waitUntilFinished();
              wait = true;
            }
          }
          if (!wait) {
            allDone = true;
          }
        }
      }
    }
    if (log_.isLogEnabled(DependenciesManager.class)) {
      log_.println("RepositoryManager finished " + sys_.lineSeperator() + 
          this);
    }
  }
  private void setRepositories(List<String> repositories ) {
    for (String repo: repositories) {
      final String repoUrl = repo;
      try {
        int status = files_.check(repoUrl);
        if (status < 300) {
          repositories_.add(repoUrl);
        }
      } catch (IOException e) {
        I_SystemMessages messages =  constants_.getSystemMessages();
        String line = messages.getTheFollowingRemoteRepositoryAppearsToBeDown();
        log_.println(line + sys_.lineSeperator() + repo + sys_.lineSeperator());
      }
    }
    if (repositories_.size() == 0) {
      I_SystemMessages messages =  constants_.getSystemMessages();
      throw new RuntimeException(messages.getNoRemoteRepositoriesCouldBeReached());
    }
  }
}
