package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.Set;

/**
 * This interface may be passed to routines if the implement I_RoutineProcessorFactoryAware.
 * It may be used to concurrently execute routines, or execute them on a single thread.
 * Also it provides a method to create traits which can be reused between
 * routines, to implement common reusable logic.
 * 
 * @author scott
 *
 */
public interface I_RoutineProcessorFactory {

  
  public abstract I_RoutineBuilder createRoutineBuilder(RoutineBriefOrigin type, 
      I_RoutinePopulator populator);

  public abstract I_RoutineExecutor createRoutineExecutor();

  public abstract I_RoutineExecutionEngine createRoutineExecutionEngine(I_FabSystem system,
      I_RoutineBuilder executorFactory);
  public abstract I_RoutinePopulatorMutant createRoutinePopulator();
  
  public abstract I_FabricationRoutine createTrait(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;
  
  public I_Fabricate getFabricate();

}