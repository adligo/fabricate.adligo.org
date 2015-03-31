package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryManager;

import java.util.List;

/**
 * Implementations of this class populate
 * routines using common information from the Fabricate run.
 * 
 * @author scott
 *
 */
public interface I_RoutinePopulator {

  public abstract void populate(I_FabricationRoutine routine);

  public abstract I_RepositoryManager getRepositoryManager();

  public abstract I_RepositoryFactory getRepositoryFactory();

  public abstract List<I_Project> getProjects();

  public abstract I_RoutineProcessorFactory getRoutineProcessorFactory();

  public abstract I_RoutineFactory getTaskFactory();

}