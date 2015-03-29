package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.dependencies.I_Dependency;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class manages the repository by downloading md5, jar 
 * and other artifact files into the repository.
 * @author scott
 *
 */
public interface I_RepositoryManager {
  
  public abstract void addDependencies(Collection<I_Dependency> dependencies);
  
  /**
   * If there was a local IOException managing the local repository 
   * doing any local IO to read, write, delete and verify files.
   * @return
   */
  public IOException getLocalException();
  /**
   * If there was a error downloading one of the dependencies,
   * this method returns a dependency which had issues downloading
   * passing a md5 check, extract check etc.
   * @return
   */
  public DependencyNotAvailableException getRemoteException();
  
  /**
   * 
   * @return false if there was a caught exception.
   */
  public abstract boolean manageDependencies();

  public abstract void checkRepositories();

  public abstract ConcurrentLinkedQueue<I_Dependency> getDependencies();


  public void logError(DependencyNotAvailableException x);
  
  

}