package org.adligo.fabricate.routines.implicit;

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

public class JarRoutine extends DependenciesQueueRoutine {
  
  private List<String> platforms_;
  
  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    setRunning();
    
    I_Project project =  projectsQueue_.poll();
    while (project != null) {
      
      waitForProjectsDependedOnToFinish(project);
      String name = brief_.getName();
      String currentProject = project.getName();
      locationInfo_.setCurrentProject(currentProject);
      
      I_RoutineBrief projectRoutineBrief = project.getStage(name);
      Iterator<String> platforms = platforms_.iterator();
      while(platforms.hasNext()) {
        String platform = platforms.next();
        String jarName = null;
        
        //do tasks
        for (TaskContext task: tasks_) {
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
            if (log_.isLogEnabled(JarRoutine.class)) {
              log_.println("starting task '" + currentTask + "' on project '" + currentProject + "' locationInfo " + 
                  locationInfo_.toString() +
                  system_.lineSeparator() +
                  this.toString()) ;
            }
            taskRoutine.run();
            if (ImplicitStages.CREATE_JAR_TASK.equals(currentTask)) {
              jarName = ((I_OutputProducer<String>)  taskRoutine).getOutput();
            }
          }
        }
      }
      notifyProjectFinished(project);
      project =  projectsQueue_.poll();
    }
  }

  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    platforms_ = (List<String>) memory.get(FabricationMemoryConstants.PLATFORMS);
    boolean toRet = super.setup(memory, routineMemory);
    return toRet;
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    platforms_ = (List<String>) memory.get(FabricationMemoryConstants.PLATFORMS);
    super.setup(memory, routineMemory);
    
  }

}
