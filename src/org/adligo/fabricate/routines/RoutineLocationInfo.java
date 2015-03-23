package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.project.I_Project;


public class RoutineLocationInfo {
  private String currentTask_;
  private String currentProject_;
  private I_Project currentWaitingProject_;
  
  public synchronized String getCurrentTask() {
    return currentTask_;
  }
  
  public synchronized String getCurrentProject() {
    return currentProject_;
  }
  
  public synchronized I_Project getWaitingProject() {
    return currentWaitingProject_;
  }

  public synchronized void setCurrentTask(String currentTask) {
    currentTask_ = currentTask;
  }
  
  public synchronized void setCurrentProject(String currentProject) {
    currentProject_ = currentProject;
  }

  public synchronized void setWaitingProject(I_Project currentWaitingProject) {
    currentWaitingProject_ = currentWaitingProject;
  }
}
