package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.files.I_FileMatcher;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.common.system.I_ExecutingProcess;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.java.JavaCParam;
import org.adligo.fabricate.java.JavaCompiler;
import org.adligo.fabricate.java.JavaFactory;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.DependencyConstants;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.I_PlatformAware;
import org.adligo.fabricate.routines.I_ProjectAware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompileSourceTask extends ProjectAwareRoutine implements I_PlatformAware {
  private I_RepositoryPathBuilder repositoryPathBuilder_;
  private I_Depot depot_;
  private String platform_;
  private String javaHome_;
  private I_FabricationRoutine findSrcTrait_;
  private JavaFactory jFactory_;
  private String missingDepotJar_;
  
  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " compiling project " + project_.getName());
    }
    repositoryPathBuilder_ = new DefaultRepositoryPathBuilder(
        fabricate_.getFabricateRepository(), files_.getNameSeparator());
    
    ((I_ProjectAware) findSrcTrait_).setProject(project_);
    findSrcTrait_.run();
    List<String> srcDirs = ((I_OutputProducer<List<String>>) findSrcTrait_).getOutput();
    
    CommonBuildDir cbd = new CommonBuildDir(files_);
    String buildDir = cbd.getBuildDir(project_);
    makeDir(buildDir);
    
    String destDir = cbd.getClassesDir(project_, platform_);
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " compiling to dir " + destDir);
    }
    makeDir(destDir);
    
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " using java home " + javaHome_);
    }
    String javaC = javaHome_ + files_.getNameSeparator() + "bin" +
        files_.getNameSeparator() + "javac";
    if (!files_.exists(javaC)) {
      String message = sysMessages_.getTheFollowingRequiredFileIsMissing();
      throw new IllegalStateException(message + system_.lineSeparator() + javaC);
    }
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " using javac " + javaC);
    }
    
    I_FileMatcher matcher = new PatternFileMatcher(files_, system_, "*/*.java", true);
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " using matcher " + matcher);
    }
    List<String> srcFiles = new ArrayList<String>();
    
    for (String srcDir: srcDirs) {
      String dir = srcDir;
      if (log_.isLogEnabled(CompileSourceTask.class)) {
        log_.println(CompileSourceTask.class.getSimpleName() + " " + project_.getName() + 
            " checking for source files in " + system_.lineSeparator() + dir + system_.lineSeparator() +
            project_.getClass().getName() );
      }
      try {
        List<String> files = files_.list(dir, matcher);
        srcFiles.addAll(files);
        if (log_.isLogEnabled(CompileSourceTask.class)) {
          log_.println(CompileSourceTask.class.getSimpleName() + " got " + files.size() + " source files in " +
              system_.lineSeparator() + dir);
        }
      } catch (IOException e1) {
        throw new RuntimeException(e1);
      }
    }
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " using " + srcFiles.size() + " source files for project " + 
          project_.getName() + ".");
    }
    String dir = project_.getDir();
    JavaCompiler jc = jFactory_.newJavaCompiler(system_, dir, javaC);
    
    StringBuilder sb = new StringBuilder();
    if (!buildClasspath(sb)) {
      throw new IllegalStateException("There was a problem building the classpath for project " + 
          project_.getName() + system_.lineSeparator() + 
          sysMessages_.getTheFollowingRequiredFileIsMissing() + system_.lineSeparator() +
          missingDepotJar_);
    }
    String cp = sb.toString();
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " project " + 
          project_.getName() + " is using the following classpath;" + system_.lineSeparator() +
          cp);
    }
    Map<JavaCParam, String> params = new HashMap<JavaCParam, String>();
    cp = cp + system_.pathSeparator() + "classes";
    params.put(JavaCParam.CP, cp);
    params.put(JavaCParam.D, destDir);
    
    int projectDirLen = project_.getDir().length();
    List<String> relSrcFiles = new ArrayList<String>();
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " changing " + srcFiles.size() + " files paths to relative file paths for project " + 
          project_.getName() + ".");
    }
    for (String srcFile: srcFiles) {
      relSrcFiles.add(srcFile.substring(projectDirLen, srcFile.length()));
    }
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " " + project_.getName() + " relative source files are as follows; " +
          system_.lineSeparator() + relSrcFiles);
    }
    try {
      if (log_.isLogEnabled(CompileSourceTask.class)) {
        log_.println(CompileSourceTask.class.getSimpleName() + " starting javac for project " + project_.getName());
      }
      I_ExecutingProcess ep = jc.compile(params, relSrcFiles);
      List<String> allOutput = new ArrayList<String>();
      boolean errors = false;
      while (!ep.isFinished()) {
        if (errors) {
          break;
        }
        try {
          ep.waitUntilFinished(1000);
        } catch (InterruptedException e) {
          system_.currentThread().interrupt();
        }
        List<String> output = ep.getOutput();
        if (output.size() >= 1) {
          allOutput.addAll(output);
          for (String out: output) {
            if (out.indexOf("error") >= 0) {
              if (log_.isLogEnabled(CompileSourceTask.class)) {
                log_.println(CompileSourceTask.class.getSimpleName() + " project " + project_.getName() + " had a compile error1.");
              }
              errors = true;
              break;
            }
          }
          if (log_.isLogEnabled(CompileSourceTask.class)) {
            StringBuilder logB = new StringBuilder();
            logB.append(CompileSourceTask.class.getSimpleName());
            logB.append(" output " + project_.getName());
            logB.append(system_.lineSeparator());
            for (String out: output) {
              logB.append(out);
              logB.append(system_.lineSeparator());
            }
            log_.println(logB.toString());
          }
        }
      }
      
      
      
      if (!errors) {
        List<String> output = ep.getOutput();
        allOutput.addAll(output);
        for (String out: output) {
          if (out.indexOf("error") >= 0) {
            errors = true;
            if (log_.isLogEnabled(CompileSourceTask.class)) {
              log_.println(CompileSourceTask.class.getSimpleName() + " project " + project_.getName() + " had a compile error2.");
            }
            break;
          }
        }
      }
      if (errors || ep.getExitCode() != 0 ) {
        log_.println(CompileSourceTask.class.getSimpleName() + " project " + project_.getName() + " is throwing a compile error.");
        
        StringBuilder exceptionB = new StringBuilder();
        for (String out: allOutput) {
          exceptionB.append(out);
          exceptionB.append(system_.lineSeparator());
        }
        //pass to run monitor
        throw new RuntimeException(exceptionB.toString());
      } else if (ep.hasFailure()) {
        throw new RuntimeException(ep.getCaught());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
  }

  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    findSrcTrait_ = createFindSrcTrait();
    if (!findSrcTrait_.setupInitial(memory, routineMemory)) {
      return false;
    }
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    javaHome_ = (String) memory.get(FabricationMemoryConstants.JAVA_HOME);
    jFactory_ = (JavaFactory) memory.get(FabricationMemoryConstants.JAVA_FACTORY);
    
    return super.setupInitial(memory, routineMemory);
  }


  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    findSrcTrait_ = createFindSrcTrait();
    findSrcTrait_.setup(memory, routineMemory);
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    javaHome_ = (String) memory.get(FabricationMemoryConstants.JAVA_HOME);
    jFactory_ = (JavaFactory) memory.get(FabricationMemoryConstants.JAVA_FACTORY);
    
    super.setup(memory, routineMemory);
  }

  private I_FabricationRoutine createFindSrcTrait() throws FabricationRoutineCreationException {
    I_FabricationRoutine ret = traitFactory_.createRoutine(FindSrcTrait.NAME, FindSrcTrait.IMPLEMENTED_INTERFACES);
    ret.setSystem(system_);
    ((I_FabricateAware) ret).setFabricate(fabricate_);
    return ret;
  }
  

  @Override
  public String getPlatform() {
    return platform_;
  }

  @Override
  public void setPlatform(String platform) {
    platform_ = platform;
  }
  
  private boolean addJar(StringBuilder sb, boolean first, String jarFilePath) {
    if (log_.isLogEnabled(CompileSourceTask.class)) {
      log_.println(CompileSourceTask.class.getSimpleName() + " project " + project_.getName() + " adding jar " +
          system_.lineSeparator() + jarFilePath);
    }
    if (!files_.exists(jarFilePath)) {
      String message = sysMessages_.getTheFollowingRequiredFileIsMissing() +
          system_.lineSeparator() + jarFilePath;
      //pass to run monitor
      throw new RuntimeException(message);
    }
    
    if (first) {
      first = false;
    } else {
      sb.append(system_.pathSeparator());
    }
    sb.append(jarFilePath);
    return first;
  }
  
  private boolean buildClasspath(StringBuilder sb) {
    missingDepotJar_ = null;
    boolean first = true;
    List<I_Dependency> deps = project_.getNormalizedDependencies();
    if (deps != null) {
      int count = 0;
      
      Iterator<I_Dependency> dit = deps.iterator();
      while(dit.hasNext()) {
        I_Dependency dep = dit.next();
        if (DependencyConstants.JAR.equalsIgnoreCase(dep.getType())) {
          String depPlat = dep.getPlatform();
          boolean add = false;
          if (depPlat == null) {
            add = true;
          } else if (platform_.equalsIgnoreCase(depPlat)) {
            add = true;
          }
          if (add) {
            String jarFilePath = repositoryPathBuilder_.getArtifactPath(new Dependency(dep));
            first = addJar(sb, first, jarFilePath);
            count++;
          }
        }
      }
      if (log_.isLogEnabled(CompileSourceTask.class)) {
        log_.println(CompileSourceTask.class.getSimpleName() + " project " + project_.getName() + " has " +
            count + " normalized dependenices.");
      }
    }
    List<I_ProjectDependency> projects = project_.getProjectDependencies();
    if (projects != null) {
      if (log_.isLogEnabled(CompileSourceTask.class)) {
        log_.println(CompileSourceTask.class.getSimpleName() + " project " + project_.getName() + " has " +
            projects.size() + " projects dependenices.");
      }
      Iterator<I_ProjectDependency> pit = projects.iterator();
      while (pit.hasNext()) {
        I_ProjectDependency dep = pit.next();
        String projectName = dep.getProjectName();
        String jarFilePath = depot_.get(projectName, "jar", platform_);
        if (jarFilePath == null) {
          throw new IllegalStateException("No jar found for project " + projectName +
              " in depot when building classpath for " + project_.getName());
        }
        if (!files_.exists(jarFilePath)) {
          missingDepotJar_ = jarFilePath;
          return false;
        }
        first = addJar(sb, first, jarFilePath);
      }
    }
    return true;
  }


}
