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
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class does a git clone for a project.
 * 
 * @author scott
 *
 */
public class GitCheckoutRoutine extends ScmContextInputAwareRoutine {

  public void run() {
    String projectsDir = fabricate_.getProjectsDir();
    String projectName = brief_.getName();
    String version = brief_.getVersion();
        
    try {
      gitCalls_.checkout(projectName, projectsDir, version);
      
    } catch (IOException x) {
      String message = sysMessages_.getThereWasAProblemCheckingOutVersionXOnTheFollowingProject();
      message = message.replaceAll("<X/>", version);
      throw new RuntimeException(message + system_.lineSeparator() +
          projectsDir + projectName, x);
    }
  }

  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    return super.setupInitial(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    super.setup(memory, routineMemory);
  }
  
}
