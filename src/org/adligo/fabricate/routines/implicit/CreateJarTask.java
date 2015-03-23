package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.java.JarParam;
import org.adligo.fabricate.java.JavaFactory;
import org.adligo.fabricate.java.JavaJar;
import org.adligo.fabricate.java.ManifestParser;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.I_PlatformAware;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateJarTask extends ProjectAwareRoutine implements I_PlatformAware, I_OutputProducer<String> {
  public static final String DEFAULT_VENDOR = "Default-Vendor";
  public static final String INCLUDE_SOURCEFILES = "includeSourceFiles";
  public static final String FILE_NAME = "fileName";
  public static final String SRC_DIRS = "srcDirs";
  
  private static final String MANIFEST = "Manifest-Version: 1.0" + System.lineSeparator() +
      "Fabrictate-Version: @FVP@" + System.lineSeparator() +
      "Created-By: @JVP@ (Oracle Corporation)" + System.lineSeparator() +
      "" + System.lineSeparator() +
      "Name: @NP@" + System.lineSeparator() +
      "Specification-Title: @STP@" + System.lineSeparator() +
      "Specification-Version: @SVP@" + System.lineSeparator() +
      "Specification-Vendor: @SOP@" + System.lineSeparator() +
      "Implementation-Title: @ITP@" + System.lineSeparator() +
      "Implementation-Version: @IVP@" + System.lineSeparator() +
      "Implementation-Vendor: @IOP@" + System.lineSeparator();
  
  private String platform_;
  private String javaHome_;
  private String jarName_;
  private JavaFactory jFactory_;
  private I_FabricationRoutine nameJarTrait_;
  
  @Override
  public String getPlatform() {
    return platform_;
  }

  @Override
  public void setPlatform(String platform) {
    platform_ = platform;
  }
  
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    nameJarTrait_ = traitFactory_.createRoutine(NameJar.NAME, NameJar.IMPLEMENTED_INTERFACES);
    javaHome_ = (String) memory.get(FabricationMemoryConstants.JAVA_HOME);
    jFactory_ = (JavaFactory) memory.get(FabricationMemoryConstants.JAVA_FACTORY);
    
    return super.setup(memory, routineMemory);
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    nameJarTrait_ = traitFactory_.createRoutine(NameJar.NAME, NameJar.IMPLEMENTED_INTERFACES);
    javaHome_ = (String) memory.get(FabricationMemoryConstants.JAVA_HOME);
    jFactory_ = (JavaFactory) memory.get(FabricationMemoryConstants.JAVA_FACTORY);
    
    super.setup(memory, routineMemory);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    CommonBuildDir cbd = new CommonBuildDir(files_);
    String inputDir = cbd.getClassesDir(project_, platform_);
    if (log_.isLogEnabled(CreateJarTask.class)) {
      log_.println(CreateJarTask.class.getSimpleName() + " creating jar " + inputDir);
    }
    Map<String,String> params = setupManifestParams();
    writeOutManifest(params, inputDir);
    
    ((I_InputAware<I_Project>) nameJarTrait_).setInput(project_);
    nameJarTrait_.run();
    jarName_ = ((I_OutputProducer<String>) nameJarTrait_).getOutput();
    
    String buildDir = cbd.getBuildDir(project_);
    String jarPathName = buildDir + files_.getNameSeparator() + jarName_;
    if (files_.exists(jarPathName)) {
      try {
        files_.delete(jarPathName);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
    String jarPath = javaHome_ + files_.getNameSeparator() + "bin" + files_.getNameSeparator() + "jar";
    JavaJar jar = jFactory_.newJavaJar(system_, buildDir, jarPath);
    
    List<JarParam> args = new ArrayList<JarParam>();
    args.add(JarParam.c);
    args.add(JarParam.f);
    args.add(JarParam.m);
    
    args.add(JarParam.C);
    
    // examples
    //   https://docs.oracle.com/javase/tutorial/deployment/jar/modman.html
    //   jar cfm jar-file manifest-addition input-file(s) 
    //
    List<String> jarParams = new ArrayList<String>();
    jarParams.add(jarName_);
    jarParams.add("ManifestInput.MF");
    
    List<String> dirs = new ArrayList<String>();
    File classesDir = files_.instance(inputDir);
    String shortClassesDir = classesDir.getName();
    dirs.add(shortClassesDir);
    
    try {
      jar.jar(args, jarParams, dirs);
    } catch (IOException e) {
      //pass to run monitor
      throw new RuntimeException(e);
    }
  }

  private Map<String,String> setupManifestParams() {
    Map<String,String> params = new HashMap<String,String>();
    String defaultVendor =  brief_.getParameterValue(DEFAULT_VENDOR);
    if ( !StringUtils.isEmpty(defaultVendor)) {
      params.put(DEFAULT_VENDOR, defaultVendor);
    } else {
      String domainName = project_.getDomainName();
      if ( !StringUtils.isEmpty(domainName)) {
        params.put(DEFAULT_VENDOR, domainName);
      } 
    }
    return params;
  }

  /***
   * @param params
   * @return
   */
  private void writeOutManifest(Map<String, String> params, String destDir) {
    CommonBuildDir cbd = new CommonBuildDir(files_);
    String inputDir = cbd.getBuildDir(project_);
    String file =  inputDir + files_.getNameSeparator() + "ManifestInput.MF";
    
    String manifestContent = MANIFEST;
    String javaVersion = system_.getJavaVersion();
    manifestContent = manifestContent.replaceAll("@JVP@", javaVersion);
    
    String fabricateVersion = fabricate_.getFabricateVersion();
    manifestContent = manifestContent.replaceAll("@FVP@", fabricateVersion);
    
    String projectName = project_.getName();
    manifestContent = manifestContent.replaceAll("@NP@", projectName);
    String defaultVendor = params.get(DEFAULT_VENDOR);
    if (StringUtils.isEmpty(defaultVendor)) {
      defaultVendor = "Unknown " + DEFAULT_VENDOR;
    }
    manifestContent = assignManifestAttribute(manifestContent, "@SOP@", 
        params.get(ManifestParser.SPECIFICATION_VENDOR),  defaultVendor);
    manifestContent = assignManifestAttribute(manifestContent, "@IOP@", 
        params.get(ManifestParser.IMPLEMENTATION_VENDOR),  defaultVendor);
    
    manifestContent = assignManifestAttribute(manifestContent, "@STP@", 
        params.get(ManifestParser.SPECIFICATION_TITLE),  projectName);
    manifestContent = assignManifestAttribute(manifestContent, "@ITP@", 
        params.get(ManifestParser.IMPLEMENTATION_TITLE),  projectName);
    
    /**
     * This is generally passed in by the calling stage class,
     * by manipulating the parameters from the fabricate.xml and
     * project.xml file, since that can sync up 
     * with the version control system's version numbering 
     * system.  Or in other words, Adligo recommends that 
     * versions are tracked with the assistance of the source control system,
     * they seem to be good at it (git, cvs).
     */
    String version = project_.getVersion();
    if (StringUtils.isEmpty(version)) {
      version = "snapshot";
    }
    manifestContent = assignManifestAttribute(manifestContent, "@SVP@", 
        params.get(ManifestParser.SPECIFICATION_VERSION),  version);
    long time = system_.getCurrentTime();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a SSS");
    String compiledDate = sdf.format(new Date(time));
    
    manifestContent = assignManifestAttribute(manifestContent, "@IVP@", 
        params.get(ManifestParser.IMPLEMENTATION_VERSION),  compiledDate);
    
    OutputStream fos = null;
    try {
      fos = files_.newFileOutputStream(file);
      fos.write(manifestContent.getBytes("UTF-8"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        fos.close();
      } catch (IOException x) {
        //do nothing
      }
    }
  }

  public String assignManifestAttribute(String manifestContent, String tag,
      String value, String defaultValue) {
    if (StringUtils.isEmpty(value)) {
      return manifestContent.replaceAll(tag, defaultValue);
    } else {
      return manifestContent.replaceAll(tag, value);
    }
  }

  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    if (I_OutputProducer.class.getName().equals(interfaceClass.getName())) {
      List<Class<?>> toRet = new ArrayList<Class<?>>();
      toRet.add(String.class);
      return toRet;
    }
    return null;
  }

  @Override
  public String getOutput() {
    return jarName_;
  }


}
