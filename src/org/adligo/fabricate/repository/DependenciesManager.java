package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * This class should concurrently obtain (download + md5 check) 
 * and extract dependencies from a ConcurrentQueue.
 * 
 * @author scott
 *
 */
public class DependenciesManager implements I_DependenciesManager {
  private final I_FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  
  private final ConcurrentLinkedQueue<I_Dependency> dependencyQueue_;
  private final I_RepositoryFactory factory_;
  
  private I_Fabricate fabricate;
  private List<String> remoteRepositories_ = new ArrayList<String>();
  private boolean onMainThread = false;
  private boolean finished = false;
  private ArrayBlockingQueue<Boolean> finished_ = new ArrayBlockingQueue<Boolean>(1);
  
  public DependenciesManager(I_FabSystem sys, 
      ConcurrentLinkedQueue<I_Dependency> deps, I_RepositoryFactory factory) {
    
    sys_ = sys;
    log_ = sys.getLog();
    files_ = sys.getFileIO();
    dependencyQueue_ = deps;
    factory_ = factory;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependenciesManager#run()
   */
  @Override
  public void run() {
    String localRepo = fabricate.getFabricateRepository();
    I_RepositoryPathBuilder builder = factory_.createRepositoryPathBuilder(localRepo, 
        files_.getNameSeparator());
    I_DependencyManager dm = factory_.createDependencyManager(sys_, 
        remoteRepositories_, builder);
    dm.setFactory(factory_);
    dm.setLocalRepository(localRepo);
    
    I_Dependency dep = dependencyQueue_.poll();
    while (dep != null) {
      dm.manange(dep);
      dep = dependencyQueue_.poll();
    }
    
    finished = true;
    try {
      finished_.put(Boolean.TRUE);
    } catch (InterruptedException e) {
      sys_.currentThread().interrupt();
    }
    if (!onMainThread) {
      sys_.join();
    }
  }

  public List<String> getRemoteRepositories() {
    return remoteRepositories_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_DependenciesManager#setRemoteRepositories(java.util.List)
   */
  @Override
  public void setRemoteRepositories(List<String> remoteRepositories) {
    remoteRepositories_.clear();
    if (remoteRepositories != null) {
      for (String rr: remoteRepositories) {
        if (rr != null) {
          remoteRepositories_.add(rr);
        }
      }
    }
  }
  public I_Fabricate getFabricate() {
    return fabricate;
  }
  public void setFabricate(I_Fabricate fabricate) {
    this.fabricate = fabricate;
  }
  public boolean isOnMainThread() {
    return onMainThread;
  }
  public void setOnMainThread(boolean onMainThread) {
    this.onMainThread = onMainThread;
  }
  @Override
  public void waitUntilFinished() {
    try {
      finished_.take();
    } catch (InterruptedException e) {
      sys_.currentThread().interrupt();
    }
  }
  
  public synchronized boolean isFinished() {
    return finished;
  }

}
