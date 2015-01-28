package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.I_StageContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.I_FileMatcher;
import org.adligo.fabricate.common.files.IncludesExcludesFileMatcher;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.external.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.external.I_RepositoryPathBuilder;
import org.adligo.fabricate.external.JavaCParam;
import org.adligo.fabricate.external.JavaCompiler;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependenciesType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.ProjectDependencyType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OldCompileTask extends OldBaseTask implements I_FabTask {
  public static final String INCLUDES = "includes";
  public static final String EXCLUDES = "excludes";
  private final I_FabSystem sys_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  
  private Map<JavaCParam,String> compilerParams_;
  private String [] srcDirs_;
  private String whichJavaC_;
  private String dir_;
  private String destDir_;
  private Map<String,List<String>> srcFiles_ = new HashMap<String,List<String>>();
  private I_RepositoryPathBuilder repositoryPathBuilder_;
  
  public OldCompileTask() {
    sys_ = new FabSystem();
    files_ = sys_.getFileIO();
    xmlFiles_ = sys_.getXmlFileIO();
  }
  
  
  @Override
  public void setup(I_RunContext ctx, NamedProject project, Map<String, String> params) {
    super.setup(ctx, project, params);
    srcDirs_ = getDelimitedValue(OldDefaultTaskHelper.SRC_DIRS, ",",params);
    if (log_.isLogEnabled(OldCompileTask.class)) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < srcDirs_.length; i++) {
        sb.append(srcDirs_[i]);
        sb.append(" ");
      }
      log_.println("Project " + project.getName() + " has srcDirs " + System.lineSeparator() +
          sb.toString());
    }
    compilerParams_ = new HashMap<JavaCParam, String>();
    Set<Entry<String,String>> entries = params.entrySet();
    for (Entry<String,String> e: entries) {
      JavaCParam cp = JavaCParam.get(e.getKey());
      if (cp != null) {
        compilerParams_.put(cp, e.getValue());
      }
    }
    dir_ = projectsPath_ + File.separator + projectName_;
    destDir_ = dir_ + File.separator + "build" + 
        File.separator + "classes";
    if (!new File(destDir_).mkdirs()) {
      lastException_ = new RuntimeException("There was a problem creating directory " + System.lineSeparator() +
          destDir_);
      return;
    }
    I_FileMatcher matcher = null;
    String inParam = params.get(INCLUDES);
    String exParam = params.get(EXCLUDES);
    if (inParam == null && exParam == null) {
      matcher = new PatternFileMatcher(files_, sys_, "*/*.java", true);
    } else {
      matcher = new IncludesExcludesFileMatcher(null, Collections.singletonList(
          new PatternFileMatcher(files_, sys_, "*/*.java", true)));
    }
    compilerParams_.put(JavaCParam.D, destDir_);
    for (int i = 0; i < srcDirs_.length; i++) {
      String srcDir = srcDirs_[i];
      List<String> files;
      try {
        files = files_.list(srcDir, matcher);
        srcFiles_.put(srcDir, files);
      } catch (IOException e1) {
        lastException_ = e1;
        return;
      }
    }
    repositoryPathBuilder_ = new DefaultRepositoryPathBuilder(ctx_.getLocalRepositoryPath(),
        File.separator);
    
    
    whichJavaC_ = ctx_.getJavaHome() + File.separator + "bin" +
        File.separator + "javac";
    compilerParams_ = Collections.unmodifiableMap(compilerParams_);
  }

  private String buildClasspath() {
    StringBuilder sb = new StringBuilder();
    sb.append("");
    
    NamedProject np = project_;
    FabricateProjectType fpt =  np.getProject();
    buildClasspath(fpt.getDependencies(), new HashSet<String>(), sb);
    return sb.toString();
  }

  public void buildClasspath(DependenciesType depsType, Set<String> completedLibraries,
      StringBuilder sb) {
    if (depsType == null) {
      return;
    }
    List<LibraryReferenceType> libraries = depsType.getLibrary();
    if (libraries != null) {
      for (LibraryReferenceType lib: libraries) {
        String libName = lib.getValue();
        if (!completedLibraries.contains(libName)) {
          completedLibraries.add(libName);
          String libFile = ctx_.getFabricateDirPath() + File.separator + "lib" +
              File.separator + libName + ".xml";
          try {
            LibraryType libType = xmlFiles_.parseLibrary_v1_0(libFile);
            DependenciesType subDepsType = libType.getDependencies();
            buildClasspath(subDepsType, completedLibraries, sb);
          } catch (IOException e) {
            throw new IllegalStateException(e);
          }
        }
      }
    }
    List<DependencyType> deps = depsType.getDependency();
    if (deps != null) {
      for (DependencyType dep: deps) {
        String jarFilePath = repositoryPathBuilder_.getPath(new Dependency(dep));
        if (sb.length() >= 1) {
          sb.append(File.pathSeparator);
        }
        sb.append(jarFilePath);
      }
    }
    
    List<ProjectDependencyType> projects = depsType.getProject();
    if (projects != null) {
      I_Depot depot = ctx_.getDepot();
      for (ProjectDependencyType project: projects) { 
        String projectName = project.getValue();
        String file = depot.get(projectName, "jar");
        if (sb.length() >= 1) {
          sb.append(File.pathSeparator);
        } 
        sb.append(file);
      }
    }
  }
  
  
  public void execute() throws IOException {
      JavaCompiler jcc = new JavaCompiler(ctx_, dir_, whichJavaC_);
      
      Map<JavaCParam,String> params = new HashMap<JavaCParam, String>();
      params.putAll(compilerParams_);
      
      I_ProjectMessages messages = constants_.getProjectMessages();
      String cp = buildClasspath();
      if (!StringUtils.isEmpty(cp)) {
        if (log_.isLogEnabled(OldCompileTask.class)) {
          log_.println("Project " + projectName_ + " has classpath" + System.lineSeparator() +
                cp); 
        }
        params.put(JavaCParam.CP, cp);
      }
      
      
      List<String> allFiles = new ArrayList<String>();
      for (int i = 0; i < srcDirs_.length; i++) {
        String srcDir = srcDirs_[i];
        if (i == 1) {
          cp = cp + File.pathSeparator + "classes";
          params.put(JavaCParam.CP, cp);
        }
        List<String> javaFiles = srcFiles_.get(srcDir);
        if (log_.isLogEnabled(OldCompileTask.class)) {
          log_.println("adding source files from " + srcDir + System.lineSeparator() +
                "\tfor compile to " + destDir_);
        }
        allFiles.addAll(javaFiles);
      }
      jcc.compile(params, allFiles);
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
