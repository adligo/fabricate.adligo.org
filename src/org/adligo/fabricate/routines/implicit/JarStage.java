package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.routines.DependenciesQueueRoutine;


/**
 * This class is just holds the default
 * RoutineBrief for the default jar stage.
 * The default jar stage uses a number of tasks to 
 * compile (CompileRoutine), obfuscate (DoNothingRoutine),
 * copy (CopyRoutine), jar (JarRoutine) 
 * and finally deposit (DepositRoutine) the jar
 * into the depot making it available for other projects.<br/>
 * <br/>
 * 
 * @author scott
 *
 */
public class JarStage {
  public static final String COMPILE = "compile";
  public static final String COPY = "copy";
  public static final String DEPOSIT = "deposit";
  public static final String JAR = "jar";
  public static final String OBSFUCATE = "obsfucate";
  
  public static final RoutineBrief JAR_STAGE = getJarStage();
  
  private static RoutineBrief getJarStage() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(DependenciesQueueRoutine.class);
    rbm.setName(JAR);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE);
    addCompile(rbm);
    
    
    return new RoutineBrief(rbm);
  }

  public static void addCompile(RoutineBriefMutant rbm) {
    RoutineBriefMutant compile = new RoutineBriefMutant();
    compile.setClazz(CompileRoutine.class);
    compile.setName(COMPILE);
    compile.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    ParameterMutant pm = new ParameterMutant();
    pm.setKey("srcDir");
    pm.setValue("src");
    compile.addParameter(pm);
    
    pm = new ParameterMutant();
    pm.setKey("outDir");
    pm.setValue("build/classes");
    compile.addParameter(pm);
    
    rbm.addNestedRoutine(compile);
  }
  
  public static void addObfuscate(RoutineBriefMutant rbm) {
    RoutineBriefMutant obsfuscate = new RoutineBriefMutant();
    obsfuscate.setClazz(DoNothingRoutine.class);
    obsfuscate.setName(OBSFUCATE);
    obsfuscate.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(obsfuscate);
  }
  
  public static void addCopy(RoutineBriefMutant rbm) {
    RoutineBriefMutant copy = new RoutineBriefMutant();
    copy.setClazz(CopyRoutine.class);
    copy.setName(COPY);
    copy.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(copy);
  }
  
  public static void addJar(RoutineBriefMutant rbm) {
    RoutineBriefMutant jar = new RoutineBriefMutant();
    jar.setClazz(JarRoutine.class);
    jar.setName(JAR);
    jar.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    
    ParameterMutant pm = new ParameterMutant();
    pm.setKey("inDir");
    pm.setValue("build/classes");
    jar.addParameter(pm);
    
    pm = new ParameterMutant();
    pm.setKey("outDir");
    pm.setValue("build");
    jar.addParameter(pm);
    
    rbm.addNestedRoutine(jar);
  }
  
  public static void addDeposit(RoutineBriefMutant rbm) {
    RoutineBriefMutant deposit = new RoutineBriefMutant();
    deposit.setClazz(DepositRoutine.class);
    deposit.setName(DEPOSIT);
    deposit.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    
    ParameterMutant pm = new ParameterMutant();
    pm.setKey("inDir");
    pm.setValue("build/classes");
    deposit.addParameter(pm);
    
    pm = new ParameterMutant();
    pm.setKey("outDir");
    pm.setValue("build");
    deposit.addParameter(pm);
    
    rbm.addNestedRoutine(deposit);
  }
  
  private JarStage() {}
}
