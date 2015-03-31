package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryManager;

import java.util.List;

public interface I_RoutinePopulatorMutant extends I_RoutinePopulator {

  public abstract void putInput(Class<?> clazz, Object input);

  public abstract void setProjects(List<I_Project> projects);

  public abstract void setRepositoryFactory(I_RepositoryFactory repositoryFactory);

  public abstract void setRepositoryManager(I_RepositoryManager repositoryManager);

  public abstract void setRoutineProcessorFactory(I_RoutineProcessorFactory routineProcessorFactory);

  public abstract void setTaskFactory(I_RoutineFactory taskFactory);

}