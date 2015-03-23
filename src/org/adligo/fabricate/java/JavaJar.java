package org.adligo.fabricate.java;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaJar {
  private I_FabSystem sys_;
  private I_FabLog log_;
  private String inDir_;
  private String jarPath_;
  
  public JavaJar(I_FabSystem sys, String inDir, String jarPath) {
    sys_ = sys;
    log_ = sys.getLog();
    inDir_ = inDir;
    jarPath_ = jarPath;
    
  }
  /**
   * 
   * @param args
   * @param params which come between the initial Jar arguments and the -C Jar arguments.
   *   This is usually the jar output name (when args contains JarParam.c) and additional files
   *   that should be included in the jar (classes, xml files, exc). 
   *   Note that since jar is usually executed from the build directory, most of the 
   *   additional content must be copied into the build directory to have it show up 
   *   correctly in the jar file, which makes the params usually contain only the 
   *   manifest input file and file name for the new jar file.
   * @param dirs
   *    This contains the list of directories to add to the jar.
   * @throws IOException
   */
  public void jar(List<JarParam> args, List<String> params, List<String> dirs) throws IOException {

    String argString = buildArgs(args);
    List<String> all = new ArrayList<String>();
    all.add(jarPath_);
    all.add(argString);
    if (params.size() >= 1) {
      all.addAll(params);
    }
    for (String dir: dirs) {
      if (args.contains(JarParam.C)) {
        all.add(JarParam.C.toString());
        all.add(dir);
        all.add(".");
      }
    }
    String [] allArray = all.toArray(new String[all.size()]);
    if (log_.isLogEnabled(JavaJar.class)) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < allArray.length; i++) {
        sb.append(allArray[i]);
        sb.append(" ");
      }
      log_.println("executing " + sb.toString());
    }
    I_Executor exe = sys_.getExecutor();
    I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
        inDir_, allArray);
    if (er.getExitCode() != 0) {
      throw new IOException(er.getOutput());
    }
  }
  
  /**
   * Ok to quote the command line help;
   * Usage: jar {ctxui}[vfmn0Me] [jar-file] [manifest-file] [entry-point] [-C dir] files ...
   * Options:
   *
   * @param whichJavaC
   * @param srcPath
   * @param params
   * @return
   */
  private String buildArgs(List<JarParam> args) {
    StringBuilder sb = new StringBuilder();
    append(sb, JarParam.c, args);
    append(sb, JarParam.t, args);
    append(sb, JarParam.x, args);
    append(sb, JarParam.u, args);
    append(sb, JarParam.i, args);
    /* this seemed to cause some hangs
    if (ctx_.isLogEnabled(JavaJar.class)) {
      sb.append("v");
    }
    */
    append(sb, JarParam.f, args);
    append(sb, JarParam.m, args);
    append(sb, JarParam.n, args);
    append(sb, JarParam.Zero, args);
    append(sb, JarParam.M, args);
    append(sb, JarParam.e, args);
    return sb.toString();
  }
  
  private void append(StringBuilder sb, JarParam param, List<JarParam> args) {
    if (args.contains(param)) {
      sb.append(param.toArg());
    }
  }
  
}
