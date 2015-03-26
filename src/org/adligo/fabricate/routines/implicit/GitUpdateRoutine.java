package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.I_ExecutingProcess;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.managers.ProjectsManager;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.ProjectMutant;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.IOException;
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
  public boolean setup(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    if (!system_.hasArg(cmdConstants_.getUpdate(true))) {
      return false;
    }
    clonedProjects_ = (ConcurrentLinkedQueue<String>) memory.get(FabricationMemoryConstants.CLONED_PROJECTS);
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
          if ( !"snapshot".equals(desc)) {
            //its on a tag, so revert to the master before update
            //note i noticed that some projects use trunk log4j for instance.
            //try to read the defaultGitBranch attribute
            String defaultBranch = "master";
            try {
              String projectFile = projectDir + files_.getNameSeparator() + "project.xml";
              FabricateProjectType fpt = xmlFiles_.parseProject_v1_0(projectFile);
              ProjectMutant pm = new ProjectMutant(projectDir, brief_, fpt);
              I_Parameter dgb = pm.getAttribute(attribConstants_.getGitDefaultBranch());
              if (dgb != null) {
                String val = dgb.getValue();
                if (!StringUtils.isEmpty(val)) {
                  defaultBranch = val;
                }
              }
            } catch (Exception e) {
              // TODO Auto-generated catch block
            }
            gitCalls_.checkout(projectName, projectsDir, defaultBranch);
          }
          I_ExecutingProcess ep = gitCalls_.pull(env_, projectName, projectsDir);
          while (!ep.isFinished()) {
            try {
              ep.waitUntilFinished(1000);
            } catch (InterruptedException e) {
              system_.currentThread().interrupt();
            }
          }
        } catch (IOException x) {
          //pass to the RunMonitor
          throw new RuntimeException(x);
        }
      }
    }
    super.run();
  }
}
