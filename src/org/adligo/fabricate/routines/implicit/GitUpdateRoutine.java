package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This routine does a git pull on a project.
 * @author scott
 *
 */
public class GitUpdateRoutine extends ScmContextInputAwareRoutine {
  private ConcurrentLinkedQueue<String> clonedProjects_;
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean setup(I_FabricationMemoryMutant memory, I_RoutineMemoryMutant routineMemory)
      throws FabricationRoutineCreationException {
    if (!system_.hasArg(cmdConstants_.getUpdate(true))) {
      return false;
    }
    clonedProjects_ = (ConcurrentLinkedQueue<String>) memory.get(FabricationMemoryConstants.CLONED_PROJECTS);
    return super.setup(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory memory, I_RoutineMemory routineMemory)
      throws FabricationRoutineCreationException {
    clonedProjects_ = (ConcurrentLinkedQueue<String>) memory.get(FabricationMemoryConstants.CLONED_PROJECTS);
    super.setup(memory, routineMemory);
  }

  @Override
  public void run() {
    String projectName = brief_.getName();
    if (clonedProjects_ != null) {
      if (!clonedProjects_.contains(projectName)) {
        //it wasn't cloned
        //check if its a tag or a branch
        String projectsDir = fabricate_.getProjectsDir();
        String projectDir = projectsDir +  projectName;
        try {
          String desc = gitCalls_.describe(projectDir);
          if (gitCalls_.isSuccess(desc)) {
            //its on a tag, so revert to the trunk before update
            gitCalls_.checkout(projectName, projectsDir, "trunk");
          }
          gitCalls_.pull(env_, projectName, projectsDir);
        } catch (IOException x) {
          //pass to the RunMonitor
          throw new RuntimeException(x);
        }
      }
    }
    super.run();
  }
  

}
