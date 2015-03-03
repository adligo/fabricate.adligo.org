package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.I_ExecutingProcess;
import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class does a git clone for a project.
 * 
 * @author scott
 *
 */
public class GitCloneRoutine extends ScmContextInputAwareRoutine {

  private ConcurrentLinkedQueue<String> clonedProjects_;
  private I_ExecutingProcess process_;
  
  public void run() {
    String projectsDir = fabricate_.getProjectsDir();
    String projectName = brief_.getName();
    if (!clonedProjects_.contains(projectName)) {
      String message = sysMessages_.getStartingGetCloneOnProjectX();
      message = message.replace("<X/>", projectName);
      log_.println(message);
      
      if (!files_.exists(projectsDir + projectName)) {
        try {
          process_ = gitCalls_.clone(env_, projectName, projectsDir);
          while (!process_.isFinished()) {
            try {
              process_.waitUntilFinished(1000);
            } catch (InterruptedException e) {
              system_.currentThread().interrupt();
            }
          }
          if (process_.hasFailure()) {
            Throwable caught = process_.getCaught();
            //pass to run monitor
            if (caught != null) {
              throw new RuntimeException(caught);
            } else {
              message = sysMessages_.getTheFollowingCommandLineProgramExitedAbnormallyWithExitCodeX();
              message = message.replace("<X/>", "" + process_.getExitCode()) + system_.lineSeperator() + "ssh-add";
              throw new IllegalStateException(message);
            }
          }
          clonedProjects_.add(projectName);
          
          message = sysMessages_.getFinishedGetCloneOnProjectX();
          message = message.replace("<X/>", projectName);
          log_.println(message);
          
          //.idx and .pack files had a read only attribute set on Windows, this is the fix
          // which allows the next Fabricate to purge (-p) the projects directory.
          I_Executor exe = system_.getExecutor();
          I_ExecutionResult result = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV, projectsDir, 
              "chmod", "-R", "+w", projectName);
          if (result.getExitCode() != 0) {
            message = sysMessages_.getTheFollowingCommandLineProgramExitedAbnormallyWithExitCodeX();
            message = message.replace("<X/>", "" + result.getExitCode());
            throw new IllegalStateException(message + system_.lineSeperator() +
                projectsDir + ": chmod -R +x " + projectName);
          }
        } catch (IOException x) {
          //pass to RunMonitor
          throw new RuntimeException(x);
        }
      }
    }
  }

  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    clonedProjects_ = new ConcurrentLinkedQueue<String>();
    memory.put(FabricationMemoryConstants.CLONED_PROJECTS, clonedProjects_);
    return super.setup(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    clonedProjects_ = (ConcurrentLinkedQueue<String>) memory.get(FabricationMemoryConstants.CLONED_PROJECTS);
    super.setup(memory, routineMemory);
  }

  @Override
  public String getCurrentLocation() {
    return super.getCurrentLocation();
  }

  @Override
  public String getAdditionalDetail() {
    if (process_ != null) {
      List<String> lines = process_.getOutput();
      if (lines != null && lines.size() >= 1) {
        return lines.get(lines.size() - 1);
      }
    }
    return super.getAdditionalDetail();
  }

}
