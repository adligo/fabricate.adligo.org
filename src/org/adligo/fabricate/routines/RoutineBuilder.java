package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class allows execution of top level routines
 * in a common way.
 * @author scott
 *
 */
public class RoutineBuilder implements I_RoutineBuilder {
  private static final Set<RoutineBriefOrigin> ALLOWED_TYPES = getAllowedTypes();
  
  public static Set<RoutineBriefOrigin> getAllowedTypes() {
    Set<RoutineBriefOrigin> ret = new HashSet<RoutineBriefOrigin>();
    ret.add(RoutineBriefOrigin.ARCHIVE_STAGE);
    ret.add(RoutineBriefOrigin.COMMAND);
    ret.add(RoutineBriefOrigin.FACET);
    ret.add(RoutineBriefOrigin.STAGE);
    ret.add(RoutineBriefOrigin.TRAIT);
    return Collections.unmodifiableSet(ret);
  }
  private final I_Fabricate fabricate_;
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private List<I_Project> projects_;
  private final RoutineBriefOrigin routineType_;
  private final I_RoutineFabricateProcessorFactory factory_;
  private final I_SystemMessages sysMessages_;
  private final I_RoutinePopulator routinePopulator_;
  /**
   * objects to pass to I_InputAware
   */
  private Map<String,Object> input_ = new HashMap<String,Object>();
  private String nextRoutineName_;
  
  public RoutineBuilder(I_FabSystem system, RoutineBriefOrigin type, 
      I_RoutineFabricateProcessorFactory factory, I_RoutinePopulator routinePopulator) {
    fabricate_ = factory.getFabricate();
    system_ = system;
    log_ = system.getLog();
    I_FabricateConstants constants = system.getConstants();
    sysMessages_ = constants.getSystemMessages();
    if (!ALLOWED_TYPES.contains(type)) {
      throw new IllegalArgumentException("type");
    }
    routineType_ = type;
    factory_ = factory;
    routinePopulator_ = routinePopulator;
  }
  
  @Override
  public I_FabricationRoutine buildInitial(I_FabricationMemoryMutant<Object> memory, 
      I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    I_FabricationRoutine toRet = processSetup();
    if (!toRet.setupInitial(memory, routineMemory)) {
      return null;
    }
    return toRet;
  }

  @Override
  public I_FabricationRoutine build(I_FabricationMemory<Object> memory, 
      I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    I_FabricationRoutine toRet = processSetup();
    toRet.setup(memory, routineMemory);
    return toRet;
  }
  
  private I_FabricationRoutine processSetup() throws FabricationRoutineCreationException {
    if (log_.isLogEnabled(RoutineBuilder.class)) {
      log_.println(RoutineBuilder.class.getSimpleName() + ".processSetup() " + nextRoutineName_);
    }
    I_FabricationRoutine routine = null;
    Set<I_ExpectedRoutineInterface> es = Collections.emptySet();
    I_RoutineBrief brief = null;
    
    switch (routineType_) {
      case ARCHIVE_STAGE:
          routine = factory_.createArchiveStage(nextRoutineName_, es);
          RoutineFactory stageFactory = factory_.getArchiveStages();
          I_RoutineFactory taskFactory = stageFactory.createTaskFactory(nextRoutineName_);
          routine.setTaskFactory(taskFactory);
          
          Map<String, I_RoutineBrief> archiveStages = fabricate_.getArchiveStages();
          brief = archiveStages.get(nextRoutineName_);
        break;
      case COMMAND:
          routine = factory_.createCommand(nextRoutineName_, es);
          RoutineFactory cmdFactory = factory_.getCommands();
          I_RoutineFactory cmdTaskFactory = cmdFactory.createTaskFactory(nextRoutineName_);
          routine.setTaskFactory(cmdTaskFactory);
          
          Map<String, I_RoutineBrief> cmds = fabricate_.getCommands();
          brief = cmds.get(nextRoutineName_);
        break;
      case FACET:
          routine = factory_.createFacet(nextRoutineName_, es);
          RoutineFactory facetsFactory = factory_.getFacets();
          I_RoutineFactory facetsTaskFactory = facetsFactory.createTaskFactory(nextRoutineName_);
          routine.setTaskFactory(facetsTaskFactory);
          
          Map<String, I_RoutineBrief> facets = fabricate_.getFacets();
          brief = facets.get(nextRoutineName_);
       break;
      case STAGE:
          routine = factory_.createStage(nextRoutineName_, es);
          RoutineFactory stagesFactory = factory_.getStages();
          I_RoutineFactory stagesTaskFactory = stagesFactory.createTaskFactory(nextRoutineName_);
          routine.setTaskFactory(stagesTaskFactory);
          
          Map<String, I_RoutineBrief> stages = fabricate_.getStages();
          brief = stages.get(nextRoutineName_);
          
        break;
      case TRAIT:
          routine = factory_.createTrait(nextRoutineName_, es);
          RoutineFactory traitsFactory = factory_.getTraits();
          I_RoutineFactory traitsTaskFactory = traitsFactory.createTaskFactory(nextRoutineName_);
          routine.setTaskFactory(traitsTaskFactory);
          
          Map<String, I_RoutineBrief> traits = fabricate_.getTraits();
          brief = traits.get(nextRoutineName_);
        break;
    }
    if (log_.isLogEnabled(RoutineBuilder.class)) {
      log_.println(RoutineBuilder.class.getSimpleName() + ".processSetup() " + nextRoutineName_ +
          " created" + system_.lineSeparator() +
          routine);
    }
    routine.setTraitFactory(factory_.getTraits());
    routinePopulator_.populate(routine);
    return routine;
  }

  public String getNextRoutineName() {
    return nextRoutineName_;
  }

  public RoutineBriefOrigin getRoutineType() {
    return routineType_;
  }

  @Override
  public void setNextRoutineName(String nextRoutineName) {
    this.nextRoutineName_ = nextRoutineName;
  }
}
