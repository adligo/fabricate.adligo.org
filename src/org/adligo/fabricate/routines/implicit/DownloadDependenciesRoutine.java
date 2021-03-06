package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.AlreadyLoggedException;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.repository.DependencyNotAvailableException;
import org.adligo.fabricate.routines.RepositoryManagerAwareRoutine;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DownloadDependenciesRoutine extends RepositoryManagerAwareRoutine {
  private static final String DEPS_QUEUE = DownloadDependenciesRoutine.class.getName() + ".dependenciesQueue";
  private ConcurrentLinkedQueue<I_Dependency> depsQueue_;

  @SuppressWarnings("unchecked")
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    List<I_Dependency> deps = (List<I_Dependency>) memory.get(FabricationMemoryConstants.DEPENDENCIES);
    depsQueue_ = system_.newConcurrentLinkedQueue(I_Dependency.class);
    for (I_Dependency dep: deps) {
      if ( !"ide".equalsIgnoreCase(dep.getType())) {
        depsQueue_.add(dep);
      }
    }
    
    routineMemory.put(DEPS_QUEUE, depsQueue_);
    return super.setupInitial(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    depsQueue_ = (ConcurrentLinkedQueue<I_Dependency>) routineMemory.get(DEPS_QUEUE);
    super.setup(memory, routineMemory);
  }

  @Override
  public void run() {
    if (depsQueue_.size() >= 1) {
      repositoryManager_.addDependencies(depsQueue_);
      if (!repositoryManager_.manageDependencies()) {
        IOException x = repositoryManager_.getLocalException();
        if (x != null) {
          log_.printTrace(x);
          throw new RuntimeException(x);
        }
        DependencyNotAvailableException caught = repositoryManager_.getRemoteException();
        if (caught != null) {
          repositoryManager_.logError(caught);
          throw new AlreadyLoggedException(caught);
        }
      }
    }
  }
}
