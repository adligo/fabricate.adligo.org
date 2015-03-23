package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.I_StageContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.java.JarParam;
import org.adligo.fabricate.java.JavaCParam;
import org.adligo.fabricate.java.JavaJar;
import org.adligo.fabricate.java.ManifestParser;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.project.I_Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This always creates a .jar file in the project/build directory.
 * @author scott
 *
 */
public class OldJarTask extends OldBaseTask implements I_FabTask {
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
      "Implementation-Vendor: @IOP@";
  
  private Map<JavaCParam,String> compilerParams_;
  private String [] srcDirs_;
  private String whichJar_;
  private String dir_;
  /**
   * the simple name of the jar 
   * with out the directory information
   */
  private String fileName_;
  private boolean includeSrc_;
  private String manifestFileName_;
  private List<JarParam> params_ = new ArrayList<JarParam>();
  
  @SuppressWarnings("boxing")
  @Override
  public void setup(I_RunContext ctx, NamedProject project, Map<String, String> params) {
    super.setup(ctx, project, params);
    
    String include = params.get(INCLUDE_SOURCEFILES);
    if (!StringUtils.isEmpty(include)) {
      includeSrc_ = Boolean.valueOf(include);
      if (includeSrc_) {
        srcDirs_ = getDelimitedValue(OldDefaultTaskHelper.SRC_DIRS, ",", params);
      }
    }
    params_.add(JarParam.c);
    
    for (JarParam p: JarParam.values()) {
      if (params.containsKey(p.toString())) {
        params_.add(p);
      }
    }
    if (!params_.contains(JarParam.M) && !params_.contains(JarParam.m)) {
      params_.add(JarParam.m);
      writeOutManifest(params);
    } else {
      
    }
    whichJar_ = ctx_.getJavaHome() + File.separator + "bin" +
        File.separator + "jar";
    dir_ = projectPath_ + File.separator + "build" + File.separator;
    
    params_.add(JarParam.f);
    fileName_ = params.get(FILE_NAME);
    if (fileName_ == null) {
      String version = params.get(ManifestParser.IMPLEMENTATION_VERSION);
      if (StringUtils.isEmpty(version)) {
        version = "snapshot";
      }
      int dotIndex = projectName_.indexOf(".");
      
      String simpleProjectName = projectName_;
      if (dotIndex != -1) {
        simpleProjectName = projectName_.substring(0, dotIndex);
      }
      fileName_ = simpleProjectName + "_" + version + ".jar";
    }
  }

  public void execute() throws IOException {
    //TODO this system should be passed
    /*
    JavaJar jar = new JavaJar(new FabSystem(), dir_, whichJar_);
    List<String> params = new ArrayList<String>();
    
    params.add(fileName_);
    params.add(manifestFileName_);
    params_.add(JarParam.C);
    
    jar.jar(params_, params , "classes");
    if (includeSrc_) {
      params_.remove(JarParam.c);
      params_.remove(JarParam.m);
      params.remove(manifestFileName_);
      
      params_.add(JarParam.u);
      for (int i = 0; i < srcDirs_.length; i++) {
        jar.jar(params_, params , srcDirs_[i]);
      }
    }
    */
  }

  /***
   * @param params
   * @return
   */
  private void writeOutManifest(Map<String, String> params) {
    manifestFileName_ = "ManifestInput.MF";
    String file = projectsPath_ + File.separator + projectName_ + File.separator +
        "build" + File.separator + manifestFileName_;
    
    String manifestContent = MANIFEST;
    String javaVersion = ctx_.getJavaVersion();
    manifestContent = manifestContent.replaceAll("@JVP@", javaVersion);
    String fabricateVersion = ctx_.getFabricateVersion();
    manifestContent = manifestContent.replaceAll("@FVP@", fabricateVersion);
    manifestContent = manifestContent.replaceAll("@NP@", projectName_);
    String defaultVendor = params.get(DEFAULT_VENDOR);
    if (StringUtils.isEmpty(defaultVendor)) {
      defaultVendor = "Unknown " + DEFAULT_VENDOR;
    }
    manifestContent = assignManifestAttribute(manifestContent, "@SOP@", 
        params.get(ManifestParser.SPECIFICATION_VENDOR),  defaultVendor);
    manifestContent = assignManifestAttribute(manifestContent, "@IOP@", 
        params.get(ManifestParser.IMPLEMENTATION_VENDOR),  defaultVendor);
    
    manifestContent = assignManifestAttribute(manifestContent, "@STP@", 
        params.get(ManifestParser.SPECIFICATION_TITLE),  projectName_);
    manifestContent = assignManifestAttribute(manifestContent, "@ITP@", 
        params.get(ManifestParser.IMPLEMENTATION_TITLE),  projectName_);
    
    /**
     * This is generally passed in by the calling stage class,
     * by manipulating the parameters from the fabricate.xml and
     * project.xml file, since that can sync up 
     * with the version control system's version numbering 
     * system.  Or in other words, Adligo recommends that 
     * versions are tracked with the assistance of the source control system,
     * they seem to be good at it (git, cvs).
     */
    manifestContent = assignManifestAttribute(manifestContent, "@SVP@", 
        params.get(ManifestParser.SPECIFICATION_VERSION),  "snapshot");
    manifestContent = assignManifestAttribute(manifestContent, "@IVP@", 
        params.get(ManifestParser.IMPLEMENTATION_VERSION),  "snapshot");
    
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      fos.write(manifestContent.getBytes("UTF-8"));
    } catch (IOException e) {
      lastException_ = e;
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

  public String getFileName() {
    return fileName_;
  }
  
  @Override
  public void setup(I_RunContext ctx, I_StageContext stageCtx, I_Project project) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void execute(I_Parameter taskParams) throws IOException {
    // TODO Auto-generated method stub
    
  }

}
