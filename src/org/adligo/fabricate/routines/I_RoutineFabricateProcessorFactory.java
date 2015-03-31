package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.fabricate.I_Fabricate;

import java.util.List;
import java.util.Set;

public interface I_RoutineFabricateProcessorFactory extends I_RoutineProcessorFactory, I_RoutineCommandProcessorFactory {
  public boolean anyArchiveStagesAssignableTo(Class<?> assignableTo, 
      List<String> commandLineArchiveStages, 
      List<String> commandArchiveSkips) throws FabricationRoutineCreationException;
  
  public boolean anyCommandsAssignableTo(Class<?> assignableTo, 
      List<String> commandLineCommands) throws FabricationRoutineCreationException;
  
  public boolean anyStagesAssignableTo(Class<?> assignableTo, 
      List<String> commandLineStages, 
      List<String> commandStagesSkips) throws FabricationRoutineCreationException;
  
  public abstract I_FabricationRoutine createArchiveStage(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;
  
  public abstract I_FabricationRoutine createFacet(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;

  public abstract I_FabricationRoutine createStage(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException;

  public RoutineFactory getArchiveStages();

  public RoutineFactory getFacets();
  public RoutineFactory getStages();
  public RoutineFactory getTraits();
  
}