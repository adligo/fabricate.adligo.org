package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.DependenciesQueueRoutine;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.I_ParticipationAware;
import org.adligo.fabricate.routines.I_PlatformAware;
import org.adligo.fabricate.routines.I_ProjectAware;
import org.adligo.fabricate.routines.I_ProjectBriefAware;
import org.adligo.fabricate.routines.TaskContext;

import java.util.Iterator;
import java.util.List;

public class JarRoutine extends DependenciesQueueRoutine implements I_ParticipationAware {
  private List<String> platforms_;
  
  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    setRunning();
    
    I_Project project =  projectsQueue_.poll();
    
    
    while (project != null) {
      if (!singleProject_) {
        waitForProjectsDependedOnToFinish(project);
      }
      String name = brief_.getName();
      String currentProject = project.getName();
      
      if (log_.isLogEnabled(JarRoutine.class)) {
        String message = sysMessages_.getStartingXOnProjectY();
        message = message.replace("<X/>", name);
        message = message.replace("<Y/>", currentProject);
        log_.println(message);
      }
      
      locationInfo_.setCurrentProject(currentProject);
      
      
      
      String platformsAttribute = project.getAttributeValue(attribConstants_.getPlatforms());
      if (platformsAttribute == null) {
        platformsAttribute = "jse";
      } else {
        platformsAttribute = platformsAttribute.toLowerCase();
      }
      
      I_RoutineBrief projectRoutineBrief = project.getStage(name);
      Iterator<String> platforms = platforms_.iterator();
      while(platforms.hasNext()) {
        String platform = platforms.next();
        platform = platform.toLowerCase();
        if (platformsAttribute.indexOf(platform) >= 0) {
          String jarName = null;
          
          //do tasks
          Iterator<TaskContext> it = tasks_.iterator();
          while (it.hasNext()) {
            TaskContext task = it.next();
            I_FabricationRoutine taskRoutine = task.getTask();
            
            I_RoutineBrief brief = taskRoutine.getBrief();
            String currentTask = brief.getName();
            locationInfo_.setCurrentProject(currentTask);
            
            boolean run = true;
            if (I_ParticipationAware.class.isAssignableFrom(taskRoutine.getClass())) {
              run = false;
              I_RoutineBrief projectTaskRoutine = projectRoutineBrief.getNestedRoutine(currentTask);
              if (projectTaskRoutine != null) {
                run = true;
              }
            }
            if (run) {
              
              if (taskRoutine instanceof I_JarFileNameAware) {
                ((I_JarFileNameAware)  taskRoutine).setJarFileName(jarName);
              }
              if (taskRoutine instanceof I_ProjectBriefAware) {
                ((I_ProjectBriefAware)  taskRoutine).setProjectBrief(project);
              }
              if (taskRoutine instanceof I_ProjectAware) {
                ((I_ProjectAware)  taskRoutine).setProject(project);
              }
              if (taskRoutine instanceof I_PlatformAware) {
                ((I_PlatformAware)  taskRoutine).setPlatform(platform);
              }
              
              taskRoutine.run();
              if (ImplicitStages.CREATE_JAR_TASK.equals(currentTask)) {
                jarName = ((I_OutputProducer<String>)  taskRoutine).getOutput();
              }
            }
          }
        }
      }
      if (log_.isLogEnabled(JarRoutine.class)) {
        String message = sysMessages_.getFinishedXOnProjectY();
        message = message.replace("<X/>", name);
        message = message.replace("<Y/>", currentProject);
        log_.println(message);
      }
      notifyProjectFinished(project);
      project =  projectsQueue_.poll();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    platforms_ = (List<String>) memory.get(FabricationMemoryConstants.PLATFORMS);
    boolean toRet = super.setupInitial(memory, routineMemory);
    return toRet;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    platforms_ = (List<String>) memory.get(FabricationMemoryConstants.PLATFORMS);
    super.setup(memory, routineMemory);
    
  }

  @Override
  public void writeToMemory(I_FabricationMemoryMutant<Object> memory) {
    depot_.store();
    super.writeToMemory(memory);
  }

}
