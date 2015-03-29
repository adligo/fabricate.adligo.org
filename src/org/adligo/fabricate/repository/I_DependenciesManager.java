package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.io.IOException;
import java.util.List;

public interface I_DependenciesManager extends Runnable {

  public IOException getLocalException();
  public DependencyNotAvailableException getRemoteException();
  public boolean isFinished();
  public boolean isOnMainThread();
  
  public abstract void setRemoteRepositories(List<String> remoteRepositories);
  public void setFabricate(I_Fabricate fabricate);
  public void setOnMainThread(boolean onMainThread);
  public void waitUntilFinished();
  
}