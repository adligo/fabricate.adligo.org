package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.AlreadyLoggedException;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.I_ProjectsAware;
import org.adligo.fabricate.routines.I_RepositoryFactoryAware;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineExecutionEngine;
import org.adligo.fabricate.routines.I_RoutinePopulatorMutant;
import org.adligo.fabricate.routines.I_RoutineProcessorFactory;
import org.adligo.fabricate.routines.I_RoutineProcessorFactoryAware;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This routine runs a git commit on a project.
 * @author scott
 *
 */
public class GitCommitCommand extends FabricateAwareRoutine 
  implements I_RoutineProcessorFactoryAware, I_RepositoryFactoryAware, I_ProjectsAware {
  public static final String NAME = "commit";
  private static final String MEMORY_MEMORY = "memory";
  
  private I_FabricationMemoryMutant<Object> memory_;
  private I_RoutineProcessorFactory routineProcessorFactory_;
  private List<I_Project> projects_;
  private ScmContext context_;
  
  @Override
  public void run() {
    super.setRunning();
    
    I_RoutineBuilder builder = routineProcessorFactory_.createRoutineBuilder(
        RoutineBriefOrigin.TRAIT, routinePopluator_);

   
    builder.setNextRoutineName(ImplicitTraits.GIT_STATUS);
    I_RoutineExecutionEngine exe = routineProcessorFactory_.createRoutineExecutionEngine(system_, builder);
    try {
      exe.runRoutines(memory_);
    } catch (FabricationRoutineCreationException e) {
      FabricationRoutineCreationException.log(log_, sysMessages_, e);
      //pass to the run monitor
      throw new AlreadyLoggedException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    commonSetup();
    memory_ = (I_FabricationMemoryMutant<Object>) routineMemory.get(MEMORY_MEMORY);
    super.setup(memory, routineMemory);
  }



  @SuppressWarnings("unchecked")
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    commonSetup();
    memory_ = memory;
    routineMemory.put(MEMORY_MEMORY, memory);
    return super.setupInitial(memory, routineMemory);
  }

  @Override
  public I_RoutineProcessorFactory getRoutineFabricateFactory() {
    return routineProcessorFactory_;
  }

  @Override
  public void setRoutineFabricateFactory(I_RoutineProcessorFactory factory) {
    routineProcessorFactory_ = factory;
  }
  
  private void commonSetup() throws FabricationRoutineCreationException {
    I_RoutineBrief scm = fabricate_.getScm();
    context_ = new ScmContext(scm);
    
  }

  @Override
  public List<I_Project> getProjects() {
    return projects_;
  }

  @Override
  public void setProjects(Collection<I_Project> projects) {
    projects_ = new ArrayList<I_Project>(projects);
  }
}
