package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineExecutionEngine;
import org.adligo.fabricate.routines.I_RoutineExecutor;
import org.adligo.fabricate.routines.I_RoutineFabricateProcessorFactory;
import org.adligo.fabricate.routines.I_RoutinePopulator;
import org.adligo.fabricate.routines.I_RoutinePopulatorMutant;
import org.adligo.fabricate.routines.RoutineBuilder;
import org.adligo.fabricate.routines.RoutineExecutionEngine;
import org.adligo.fabricate.routines.RoutineExecutor;
import org.adligo.fabricate.routines.RoutineFactory;
import org.adligo.fabricate.routines.RoutinePopulatorMutant;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class generates routine implementations for use by the presenters.  
 * It is NOT aware of project specific settings.  
 * @author scott
 *
 */
public class ImplicitRoutineFactory implements I_RoutineFabricateProcessorFactory {
  private static final Set<I_ExpectedRoutineInterface> EMPTY_SET = Collections.emptySet();
  private final I_Fabricate fabricate;
  
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final RoutineFactory archiveStages_;
  private final RoutineFactory commands_;
  private final RoutineFactory facets_;
  private final RoutineFactory stages_;
  private final RoutineFactory traits_;
  
  public ImplicitRoutineFactory(I_FabSystem system, I_Fabricate fab, boolean commandsNotStages) {
    system_ = system;
    log_ = system.getLog();
    archiveStages_ = new RoutineFactory(system);
    facets_ = new RoutineFactory(system);
    commands_ = new RoutineFactory(system);
    stages_ = new RoutineFactory(system);
    traits_ = new RoutineFactory(system);
    fabricate = fab;
    
    addImplicitFacets();
    if (commandsNotStages) {
      addImplicitCommands();
      Map<String, I_RoutineBrief> fabCmds = fab.getCommands();
      Collection<I_RoutineBrief>  briefs = fabCmds.values();
      for (I_RoutineBrief brief: briefs) {
        commands_.add(brief);
      }
    } else {
      addImplicitStages();
      Map<String, I_RoutineBrief> fabStages = fab.getStages();
      Collection<I_RoutineBrief>  briefs = fabStages.values();
      for (I_RoutineBrief brief: briefs) {
        commands_.add(brief);
      }
    }
    addImplicitTraits();
    
    Map<String, I_RoutineBrief> cmds = fab.getCommands();
    if (cmds != null & cmds.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> commands = cmds.entrySet();
      for (Entry<String,I_RoutineBrief> command: commands) {
        commands_.add(command.getValue());
      }
    }
    /*
    Map<String, I_RoutineBrief> facets = fab.getF
    if (cmds != null & cmds.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> commands = cmds.entrySet();
      for (Entry<String,I_RoutineBrief> command: commands) {
        commands_.add(command.getValue());
      }
    }
    */
    
    Map<String, I_RoutineBrief> stages = fab.getStages();
    if (stages != null & stages.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> stageEntries = stages.entrySet();
      for (Entry<String,I_RoutineBrief> stage: stageEntries) {
        stages_.add(stage.getValue());
      }
    }
    
    Map<String, I_RoutineBrief> traits = fab.getTraits();
    if (traits != null & traits.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> traitEntries = traits.entrySet();
      for (Entry<String,I_RoutineBrief> trait: traitEntries) {
        traits_.add(trait.getValue());
      }
    }
  }
  
  public void addImplicitFacets() {
    List<I_RoutineBrief> briefs = ImplicitFacets.ALL;
    
    for (I_RoutineBrief brief: briefs) {
      facets_.add(brief);
    }
  }
  
  public void addImplicitCommands() {
    List<I_RoutineBrief> briefs = ImplicitCommands.ALL;
    
    for (I_RoutineBrief brief: briefs) {
      commands_.add(brief);
    }
  }

  public void addImplicitStages() {
    List<I_RoutineBrief> briefs = ImplicitStages.ALL;
    
    for (I_RoutineBrief brief: briefs) {
      stages_.add(brief);
    }
  }
  
  public void addImplicitTraits() {
    List<I_RoutineBrief> briefs = ImplicitTraits.ALL;
    
    for (I_RoutineBrief brief: briefs) {
      traits_.add(brief);
    }
  }
  /**
   * TODO extract this method into a class and reuse for 
   * anyStagesAssignableTo and write tests.
   * 
   * returns true if a archive stage is assignable to the assignableTo class parameter.
   * @param assignableTo
   * @return
   * @throws FabricationRoutineCreationException
   */
  public boolean anyArchiveStagesAssignableTo(Class<?> assignableTo, 
      List<String> commandLineStages, 
      List<String> commandStagesSkips) throws FabricationRoutineCreationException {
    

    for (String name: commandLineStages) {
      I_FabricationRoutine fr = archiveStages_.createRoutine(name, EMPTY_SET);
      if (assignableTo.isAssignableFrom(fr.getClass())) {
        if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
          log_.println("The following archive stages are NOT assignable to " + assignableTo + system_.lineSeparator() +
              commandLineStages + system_.lineSeparator() +
              commandStagesSkips);
        }
        return true;
      }
    }
    List<I_RoutineBrief> routines = archiveStages_.getValues();
    
    for (I_RoutineBrief routine: routines) {
      String name = routine.getName();
      if (!commandLineStages.contains(name) && !commandStagesSkips.contains(name)) {
        if (!routine.isOptional()) {
          I_FabricationRoutine fr = archiveStages_.createRoutine(name, EMPTY_SET);
          if (assignableTo.isAssignableFrom(fr.getClass())) {
            if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
              log_.println("The following archive stages are assignable to " + assignableTo + system_.lineSeparator() +
                  commandLineStages + system_.lineSeparator() +
                  commandStagesSkips);
            }
            return true;
          }
        }
      }
    }
    if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
      log_.println("The following stages are NOT assignable to " + assignableTo + system_.lineSeparator() +
          commandLineStages + system_.lineSeparator() +
          commandStagesSkips);
    }
    return false;
  }
  
  /**
   * returns true if a command is assignable to the assignableTo class parameter.
   * @param assignableTo
   * @return
   * @throws FabricationRoutineCreationException
   */
  public boolean anyCommandsAssignableTo(Class<?> assignableTo, 
      List<String> commandLineCommands) throws FabricationRoutineCreationException {
   
    for (String name: commandLineCommands) {
      I_FabricationRoutine fr = commands_.createRoutine(name, EMPTY_SET);
      if (assignableTo.isAssignableFrom(fr.getClass())) {
        if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
          log_.println("The following commands are assignable to " + assignableTo + system_.lineSeparator() +
              commandLineCommands);
        }
        return true;
      }
    }
    if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
      log_.println("The following commands are NOT assignable to " + assignableTo + system_.lineSeparator() +
          commandLineCommands);
    }
    return false;
  }
  
  /**
   * TODO extract this method into a class and reuse for 
   * anyArchiveStagesAssignableTo and write tests.
   * 
   * returns true if a stage is assignable to the assignableTo class parameter.
   * @param assignableTo
   * @return
   * @throws FabricationRoutineCreationException
   */
  public boolean anyStagesAssignableTo(Class<?> assignableTo, 
      List<String> commandLineStages, 
      List<String> commandStagesSkips) throws FabricationRoutineCreationException {
   

    for (String name: commandLineStages) {
      I_FabricationRoutine fr = stages_.createRoutine(name, EMPTY_SET);
      if (assignableTo.isAssignableFrom(fr.getClass())) {
        if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
          log_.println("The following stages are NOT assignable to " + assignableTo + system_.lineSeparator() +
              commandLineStages + system_.lineSeparator() +
              commandStagesSkips);
        }
        return true;
      }
    }
    List<I_RoutineBrief> routines = stages_.getValues();
    
    for (I_RoutineBrief routine: routines) {
      String name = routine.getName();
      if (!commandLineStages.contains(name) && !commandStagesSkips.contains(name)) {
        if (!routine.isOptional()) {
          I_FabricationRoutine fr = stages_.createRoutine(name, EMPTY_SET);
          if (assignableTo.isAssignableFrom(fr.getClass())) {
            if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
              log_.println("The following stages are assignable to " + assignableTo + system_.lineSeparator() +
                  commandLineStages + system_.lineSeparator() +
                  commandStagesSkips);
            }
            return true;
          }
        }
      }
    }
    if (log_.isLogEnabled(ImplicitRoutineFactory.class)) {
      log_.println("The following stages are NOT assignable to " + assignableTo + system_.lineSeparator() +
          commandLineStages + system_.lineSeparator() +
          commandStagesSkips);
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createArchiveStage(org.adligo.fabricate.common.system.I_FabSystem, org.adligo.fabricate.routines.I_RoutineBuilder)
   */
  @Override
  public I_FabricationRoutine createArchiveStage(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return archiveStages_.createRoutine(name, interfaces);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createCommand(org.adligo.fabricate.common.system.I_FabSystem, org.adligo.fabricate.routines.I_RoutineBuilder)
   */
  @Override
  public I_FabricationRoutine createCommand(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return commands_.createRoutine(name, interfaces);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createFacet(java.lang.String, java.util.Set)
   */
  @Override
  public I_FabricationRoutine createFacet(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return facets_.createRoutine(name, interfaces);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createRoutineBuilder(org.adligo.fabricate.models.common.RoutineBriefOrigin)
   */
  @Override
  public I_RoutineBuilder createRoutineBuilder(RoutineBriefOrigin type, I_RoutinePopulator pop) {
    return new RoutineBuilder(system_, type, this, pop);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createRoutineExecutionEngine(org.adligo.fabricate.common.system.I_FabSystem, org.adligo.fabricate.routines.I_RoutineBuilder)
   */
  @Override
  public I_RoutineExecutionEngine createRoutineExecutionEngine(I_FabSystem system, I_RoutineBuilder executorFactory) {
     return new RoutineExecutionEngine(system, executorFactory, fabricate.getThreads());
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createStage(java.lang.String, java.util.Set)
   */
  @Override
  public I_FabricationRoutine createStage(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return stages_.createRoutine(name, interfaces);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.routines.implicit.I_RoutineProcessorFactory#createTrait(java.lang.String, java.util.Set)
   */
  @Override
  public I_FabricationRoutine createTrait(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return traits_.createRoutine(name, interfaces);
  }

  public RoutineFactory getArchiveStages() {
    return archiveStages_;
  }
  public RoutineFactory getCommands() {
    return commands_;
  }
  public I_Fabricate getFabricate() {
    return fabricate;
  }
  public RoutineFactory getFacets() {
    return facets_;
  }

  public RoutineFactory getStages() {
    return stages_;
  }
  public RoutineFactory getTraits() {
    return traits_;
  }
  
  
  private void addTrait(String name, Class<? extends I_FabricationRoutine> clazz) {
    RoutineBriefMutant bm = new RoutineBriefMutant();
    bm.setName(name);
    bm.setClazz(clazz);
    bm.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT);
    traits_.add(new RoutineBrief(bm));
  }

  @Override
  public I_RoutineExecutor createRoutineExecutor() {
    return new RoutineExecutor(system_, this);
  }

  @Override
  public I_RoutinePopulatorMutant createRoutinePopulator() {
    return new RoutinePopulatorMutant(system_, this);
  }
  
  
}
