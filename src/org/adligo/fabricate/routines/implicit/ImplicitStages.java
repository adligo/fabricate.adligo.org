package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
  
  /**
   * all must be at the bottom, so that instances can be init
   */
  public static final List<I_RoutineBrief> ALL = getAll();
  
  private static List<I_RoutineBrief> getAll() {
    List<I_RoutineBrief> ret = new ArrayList<I_RoutineBrief>();
    
    try {
      ret.add(JAR_BRIEF);    
    } catch (Exception x) {
      throw new RuntimeException(x);
    }
    return Collections.unmodifiableList(ret);
  }
  
  private static RoutineBrief getJarBrief() {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(JarRoutine.class);
    rbm.setName(JAR);
    rbm.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE);
    
    RoutineBriefMutant gen = new RoutineBriefMutant();
    gen.setClazz(LoadProjectTask.class);
    gen.setName(GENERATE_SOURCE_TASK);
    gen.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(gen);
    
    RoutineBriefMutant comp = new RoutineBriefMutant();
    comp.setClazz(CompileSourceTask.class);
    comp.setName(COMPILE_SOURCE_TASK);
    comp.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(comp);
    
    //leaving obfuscate out of the implicit jar stage,
    // users may add it in fabricate.xml if they want
    // obfuscation.
    
    RoutineBriefMutant add = new RoutineBriefMutant();
    add.setClazz(AddFilesTask.class);
    add.setName(ADD_FILES_TASK);
    add.setOrigin(RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
    rbm.addNestedRoutine(add);
    
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
