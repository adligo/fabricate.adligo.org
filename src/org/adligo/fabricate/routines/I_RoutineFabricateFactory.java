package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.Set;

public interface I_RoutineFabricateFactory {
  public I_RoutineExecutor createRoutineExecutor(I_FabSystem system, I_RoutineFabricateFactory factory);
  
  public abstract I_FabricationRoutine createArchiveStage(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;
  
  public abstract I_FabricationRoutine createCommand(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;
  
  public abstract I_FabricationRoutine createFacet(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;

  public abstract RoutineExecutionEngine createRoutineExecutionEngine(I_FabSystem system,
      I_RoutineBuilder executorFactory);

  public abstract I_FabricationRoutine createStage(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;

  public abstract I_FabricationRoutine createTrait(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;

  public RoutineFactory getArchiveStages();
  public RoutineFactory getCommands() ;
  public I_Fabricate getFabricate();
  public RoutineFactory getFacets();
  public RoutineFactory getStages();
  public RoutineFactory getTraits();
  
}