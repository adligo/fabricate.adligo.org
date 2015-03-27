package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.java.JavaCalls;
import org.adligo.fabricate.java.JavaFactory;
import org.adligo.fabricate.models.common.AttributesOverlay;
import org.adligo.fabricate.models.common.ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_AttributesOverlay;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.I_ProjectAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This trait discovers which source dirs are to be used 
 * for a project in a common way (so it can be extracted 
 * from ComplileSourceTask and reused in fabricate4eclipse
 * to make FabricateClasspathToEclipse4ishClasspathConverter
 * use the same exact code as ComplileSourceTask).
 * 
 * @author scott
 *
 */
public class FindSrcTrait extends ProjectAwareRoutine implements
  I_OutputProducer<List<String>> {
  public static final String NAME = "find src";
  public static final Set<I_ExpectedRoutineInterface> IMPLEMENTED_INTERFACES = getInterfaces();
  
  private static Set<I_ExpectedRoutineInterface> getInterfaces() {
    Set<I_ExpectedRoutineInterface> toRet = new HashSet<I_ExpectedRoutineInterface>();
    toRet.add(new ExpectedRoutineInterface(I_ProjectAware.class));
    toRet.add(new ExpectedRoutineInterface(I_FabricateAware.class));
    toRet.add(new ExpectedRoutineInterface(I_OutputProducer.class, List.class));
    return Collections.unmodifiableSet(toRet);
  }
  
  private JavaFactory jFactory_;
  private List<String> output_= new ArrayList<String>();
  
  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    if (I_OutputProducer.class.getName().equals(interfaceClass.getName())) {
      return newClassList(List.class);
    }
    return null;
  }

  @Override
  public List<String> getOutput() {
    return new ArrayList<String>(output_);
  }

  @Override
  public void run() {
    output_.clear();
    
    String projectDir = project_.getDir();
    if (log_.isLogEnabled(FindSrcTrait.class)) {
      log_.println(FindSrcTrait.class.getName() + " checking for source for project " +
          project_.getName() + system_.lineSeparator() +
          projectDir);
    }
    String srcDirsKey = attribConstants_.getSrcDirs();
    I_AttributesOverlay overlay = new AttributesOverlay(fabricate_, project_);
    I_Parameter firstSrcDirs = overlay.getAttribute(srcDirsKey);
    if (firstSrcDirs == null || StringUtils.isEmpty(firstSrcDirs.getValue())) {
      output_.add(projectDir + "src");
    } else {
      String [] srcDirs = firstSrcDirs.getValueDelimited(",");
      if (srcDirs.length == 1) {
        if (StringUtils.isEmpty(srcDirs[0])) {
          output_.add(projectDir + "src");
        }
      } 
      if (output_.size() == 0) {
        for (int i = 0; i < srcDirs.length; i++) {
          String dir = srcDirs[i];
          output_.add(projectDir + dir);
        }
      }
    }
    
    String jdkSrcDirKey = attribConstants_.getJdkSrcDir();
    I_Parameter jdkSrcDir = overlay.getAttribute(jdkSrcDirKey);
    if (jdkSrcDir != null) {
      String dir = jdkSrcDir.getValue();
      if (dir != null) {
        JavaCalls jc =  jFactory_.newJavaCalls(system_);
        String javaVersion = system_.getJavaVersion();
        String majorVersion = "" + jc.getJavaMajorVersion(javaVersion);
        
        output_.add(projectDir + dir + majorVersion);
      }
    }
  }

  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    jFactory_ = (JavaFactory) memory.get(FabricationMemoryConstants.JAVA_FACTORY);
    
    return super.setupInitial(memory, routineMemory);
  }


  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    jFactory_ = (JavaFactory) memory.get(FabricationMemoryConstants.JAVA_FACTORY);
    
    super.setup(memory, routineMemory);
  }
}
