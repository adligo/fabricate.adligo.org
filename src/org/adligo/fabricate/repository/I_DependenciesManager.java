package org.adligo.fabricate.repository;

import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.List;

public interface I_DependenciesManager extends Runnable {

  public abstract void setRemoteRepositories(List<String> remoteRepositories);
  public void setFabricate(I_Fabricate fabricate);
  
  public boolean isOnMainThread();
  public void setOnMainThread(boolean onMainThread);
  public void waitUntilFinished();
  public boolean isFinished();
}