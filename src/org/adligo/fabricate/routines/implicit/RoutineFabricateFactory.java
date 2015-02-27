package org.adligo.fabricate.routines.implicit;

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
import org.adligo.fabricate.routines.RoutineExecutionEngine;
import org.adligo.fabricate.routines.RoutineFactory;

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
public class RoutineFabricateFactory {
  private static final Set<I_ExpectedRoutineInterface> EMPTY_SET = Collections.emptySet();
  private final I_Fabricate fabricate;
  private final RoutineFactory traits_;
  private final RoutineFactory commands_;
  private final RoutineFactory stages_;

  public RoutineFabricateFactory(I_FabSystem system, I_Fabricate fab, boolean commandsNotStages) {
    traits_ = new RoutineFactory(system);
    commands_ = new RoutineFactory(system);
    stages_ = new RoutineFactory(system);
    fabricate = fab;
    if (commandsNotStages) {
      addImplicitCommands();
    } else {
      addImplicitStages();
    }
    addImplicitTraits();
    
    Map<String, I_RoutineBrief> cmds = fab.getCommands();
    if (cmds != null & cmds.size() >= 1) {
      Set<Entry<String, I_RoutineBrief>> commands = cmds.entrySet();
      for (Entry<String,I_RoutineBrief> command: commands) {
        commands_.add(command.getValue());
      }
    }
    
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
  public void addImplicitCommands() {
    addCommand(EncryptCommand.NAME, EncryptCommand.class);
    addCommand(DecryptCommand.NAME, DecryptCommand.class);
    addCommand(PublishCommand.NAME, PublishCommand.class);
  }

  public void addImplicitStages() {
    stages_.add(JarStage.JAR_STAGE);
  }
  
  public void addImplicitTraits() {
    addTrait(EncryptTrait.NAME, EncryptTrait.class);
    addTrait(DecryptTrait.NAME, DecryptTrait.class);
    addTrait(GitCloneRoutine.NAME, GitCloneRoutine.class);
    addTrait(GitPullRoutine.NAME, GitPullRoutine.class);
    addTrait(GitCommitRoutine.NAME, GitCommitRoutine.class);
    addTrait(GitPushRoutine.NAME, GitPushRoutine.class);
    traits_.add(ObtainTrait.ROUTINE_BRIEF);
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
        return true;
      }
    }
    return false;
  }
  
  /**
   * returns true if a stage or command is assignable to the assignableTo class parameter.
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
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public I_FabricationRoutine createCommand(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return commands_.createRoutine(name, interfaces);
  }
  
  public RoutineExecutionEngine createRoutineExecutionEngine(I_FabSystem system, I_RoutineBuilder executorFactory) {
     return new RoutineExecutionEngine(system, executorFactory, fabricate.getThreads());
  }
  
  public I_FabricationRoutine createStage(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return stages_.createRoutine(name, interfaces);
  }
  
  public I_FabricationRoutine createTrait(String name,Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    return traits_.createRoutine(name, interfaces);
  }

  public I_Fabricate getFabricate() {
    return fabricate;
  }
  public RoutineFactory getTraits() {
    return traits_;
  }
  public RoutineFactory getCommands() {
    return commands_;
  }
  public RoutineFactory getStages() {
    return stages_;
  }
  
  private void addCommand(String name, Class<? extends I_FabricationRoutine> clazz) {
    RoutineBriefMutant bm = new RoutineBriefMutant();
    bm.setName(name);
    bm.setClazz(clazz);
    bm.setOrigin(RoutineBriefOrigin.IMPLICIT_COMMAND);
    commands_.add(new RoutineBrief(bm));
  }
  
  private void addTrait(String name, Class<? extends I_FabricationRoutine> clazz) {
    RoutineBriefMutant bm = new RoutineBriefMutant();
    bm.setName(name);
    bm.setClazz(clazz);
    bm.setOrigin(RoutineBriefOrigin.IMPLICIT_TRAIT);
    traits_.add(new RoutineBrief(bm));
  }
}
