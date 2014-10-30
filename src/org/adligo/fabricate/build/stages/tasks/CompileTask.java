package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.external.JavaCParam;
import org.adligo.fabricate.external.JavaCompilerCalls;
import org.adligo.fabricate.external.files.FileUtils;
import org.adligo.fabricate.external.files.I_FileMatcher;
import org.adligo.fabricate.external.files.IncludesExcludesFileMatcher;
import org.adligo.fabricate.external.files.PatternFileMatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

public class CompileTask extends BaseTask implements I_FabTask {
  public static final String SRC_DIRS = "srcDirs";
  public static final String INCLUDES = "includes";
  public static final String EXCLUDES = "excludes";
  private Map<JavaCParam,String> compilerParams_;
  private String [] srcDirs_;
  private String whichJavaC_;
  private String dir_;
  private String destDir_;
  private Map<String,List<String>> srcFiles_ = new HashMap<String,List<String>>();
  
  @Override
  public void setup(I_FabContext ctx, NamedProject project, Map<String, String> params) {
    super.setup(ctx, project, params);
    String srcDirsParams = params.get(SRC_DIRS);
    
    if (StringUtils.isEmpty(srcDirsParams)) {
      srcDirs_ = new String[] {"src"};
    } else {
      StringTokenizer st = new StringTokenizer(srcDirsParams, ",");
      List<String> tokens = new ArrayList<String>();
      while (st.hasMoreTokens()) {
        tokens.add(st.nextToken());
      }
      srcDirs_ = tokens.toArray(new String[tokens.size()]);
    }
    for (int i = 0; i < srcDirs_.length; i++) {
      srcDirs_[i] = projectsPath_ + File.separator + projectName_ + File.separator +
          srcDirs_[i];
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
      matcher = new PatternFileMatcher(ctx_, "*/*.java", true);
    } else {
      matcher = new IncludesExcludesFileMatcher(ctx_, inParam, "*/*.java", exParam, null);
    }
    FileUtils fus = new FileUtils(ctx_);
    compilerParams_.put(JavaCParam.D, destDir_);
    for (int i = 0; i < srcDirs_.length; i++) {
      String srcDir = srcDirs_[i];
      List<String> files;
      try {
        files = fus.list(new File(srcDir).toPath(), matcher);
        srcFiles_.put(srcDir, files);
      } catch (IOException e1) {
        lastException_ = e1;
        return;
      }
    }
    
    whichJavaC_ = ctx_.getJavaHome() + File.separator + "bin" +
        File.separator + "javac";
  }

  public void execute() throws IOException {
    JavaCompilerCalls jcc = new JavaCompilerCalls(dir_, whichJavaC_);
    for (int i = 0; i < srcDirs_.length; i++) {
      String srcDir = srcDirs_[i];
      List<String> javaFiles = srcFiles_.get(srcDir);
      if (ctx_.isLogEnabled(CompileTask.class)) {
        ThreadLocalPrintStream.println("compiling from " + srcDir + System.lineSeparator() +
              "\tto " + destDir_);
      }
      jcc.compile(srcDir, compilerParams_, javaFiles);
    }
    
  }

}
