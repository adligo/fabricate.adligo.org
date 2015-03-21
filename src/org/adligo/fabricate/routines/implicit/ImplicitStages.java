package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.routines.DependenciesQueueRoutine;

/**
 * This class just contains the routine brief
 * for the obtain trait.
 * @author scott
 *
 */
public class ImplicitStages {
  public static final String GENERATE_SOURCE_TASK = "generate source";
  public static final String COMPILE_SOURCE_TASK = "compile source";
  public static final String ADD_FILES_TASK = "add files";
  public static final String CREATE_JAR_TASK = "create jar";
  public static final String DEPOSIT_TASK = "deposit jar";
  public static final String JAR = "jar";
  public static final RoutineBrief JAR_BRIEF = getJarBrief();
  
  
  private static RoutineBrief getJarBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(DependenciesQueueRoutine.class);
    rbm.setName(JAR);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE);
    
    RoutineBriefMutant gen = new RoutineBriefMutant();
    gen.setClazz(LoadProjectTask.class);
    gen.setName(GENERATE_SOURCE_TASK);
    gen.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(gen);
    
    RoutineBriefMutant comp = new RoutineBriefMutant();
    comp.setClazz(CompileTask.class);
    comp.setName(COMPILE_SOURCE_TASK);
    comp.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(comp);
    
    //leaving obfuscate out of the implicit jar stage,
    // users may add it in fabricate.xml if they want
    // obfuscation.
    
    RoutineBriefMutant jar = new RoutineBriefMutant();
    jar.setClazz(CreateJarTask.class);
    jar.setName(CREATE_JAR_TASK);
    jar.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(jar);
    
    RoutineBriefMutant deposit = new RoutineBriefMutant();
    deposit.setClazz(DepositJarTask.class);
    deposit.setName(DEPOSIT_TASK);
    deposit.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(deposit);
    
    return new RoutineBrief(rbm);
  }
  
}
