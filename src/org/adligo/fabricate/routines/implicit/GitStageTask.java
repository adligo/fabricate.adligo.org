package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.models.project.ProjectModifications;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_ProjectBriefAware;
import org.adligo.fabricate.routines.I_ProjectsAware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This task is intended to be run by a ProjectsQueueRoutine.
 * @author scott
 *
 */
public class GitStageTask extends ScmContextInputAwareRoutine 
  implements I_ProjectBriefAware, I_InputAware<ScmContext> {
  public static final Set<I_ExpectedRoutineInterface> IMPLEMENTED_INTERFACES = getImplementedInterfaces();
  private static final String MODIFICATIONS = GitStageTask.class.getName() + ".modifications";
  
  private ConcurrentLinkedQueue<ProjectModifications> modifications_;
  private I_ProjectBrief project_;
  
  
  private static Set<I_ExpectedRoutineInterface> getImplementedInterfaces() {
    Set<I_ExpectedRoutineInterface> ret = new HashSet<I_ExpectedRoutineInterface>();
    
    ret.add(new ExpectedRoutineInterface(I_InputAware.class, ScmContext.class));
    ret.add(new ExpectedRoutineInterface(I_FabricateAware.class));
    ret.add(new ExpectedRoutineInterface(I_ProjectBriefAware.class));
    ret.add(new ExpectedRoutineInterface(I_ProjectsAware.class));
    return Collections.unmodifiableSet(ret);
  }
  
  @Override
  public void run() {
    super.setRunning();
    
    String projectsDir = fabricate_.getProjectsDir();
    String projectName = project_.getName();
    
    try {
     
      List<String> result = gitCalls_.status(projectsDir + projectName);
      if (log_.isLogEnabled(GitStageTask.class)) {
        StringBuilder sb = new StringBuilder();
        sb.append(GitStageTask.class.getName() + system_.lineSeparator() +
            "git status returned for project " + projectName);
        sb.append(system_.lineSeparator());
        for (String line: result) {
          sb.append(line);
          sb.append(system_.lineSeparator());
        }
        log_.println(sb.toString());
      }
    } catch (IOException x) {
      //pass to RunMonitor
      throw new RuntimeException(x);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
   
    modifications_ = (ConcurrentLinkedQueue<ProjectModifications>) routineMemory.get(MODIFICATIONS);
    super.setup(memory, routineMemory);
  }

  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    modifications_ = system_.newConcurrentLinkedQueue(ProjectModifications.class);
    routineMemory.put(MODIFICATIONS, modifications_);
    return super.setupInitial(memory, routineMemory);
  }

  @Override
  public void writeToMemory(I_FabricationMemoryMutant<Object> memory) {
    
    List<ProjectModifications> mods = new ArrayList<ProjectModifications>(modifications_);
    memory.put(FabricationMemoryConstants.PROJECTS_MODIFIED, 
        Collections.unmodifiableList(mods));
    super.writeToMemory(memory);
  }

  @Override
  public I_ProjectBrief getProjectBrief() {
    return project_;
  }

  @Override
  public void setProjectBrief(I_ProjectBrief project) {
    project_ = project;
  }

}
