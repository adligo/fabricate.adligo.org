package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_ParticipationAware;
import org.adligo.fabricate.routines.I_ProjectAware;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This task is a place holder which calls the 'generate source' trait
 * for the project (which may be set in fabricate.xml or project.xml).
 * The idea here is that some projects may want to generate jaxb, xml_io
 * or other source before the java compilation begins.
 * @author scott
 *
 */
public class GenerateSourceTask extends ProjectAwareRoutine implements I_ParticipationAware {
  private static final String GENERATE_SOURCE = "generate source";
  private static final Set<I_ExpectedRoutineInterface> GENERATE_SOURCE_INTERFACES = getGenerateSourceInterfaces();
  
  private static Set<I_ExpectedRoutineInterface> getGenerateSourceInterfaces() {
    Set<I_ExpectedRoutineInterface> toRet = new HashSet<I_ExpectedRoutineInterface>();
    toRet.add(new ExpectedRoutineInterface(I_FabricateAware.class));
    toRet.add(new ExpectedRoutineInterface(I_ProjectAware.class));
    return Collections.unmodifiableSet(toRet);
  }
  private I_FabricationRoutine traitRoutine_;
  
  @Override
  public void run() {
    
    ((I_FabricateAware) traitRoutine_).setFabricate(fabricate_);
    ((I_ProjectAware) traitRoutine_).setProject(project_);
    traitRoutine_.run();
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    traitRoutine_ = (I_FabricationRoutine) routineMemory.get(GENERATE_SOURCE);
    super.setup(memory, routineMemory);
  }

  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    traitRoutine_ = traitFactory_.createRoutine(GENERATE_SOURCE, GENERATE_SOURCE_INTERFACES);
    routineMemory.put(GENERATE_SOURCE, traitRoutine_);
    return super.setupInitial(memory, routineMemory);
  }

}
