package org.adligo.fabricate.external;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.ThreadLocalPrintStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaJar {
  private I_FabContext ctx_;
  private String inDir_;
  private String javaC_;
  
  public JavaJar(I_FabContext ctx, String inDir, String javaC) {
    ctx_ = ctx;
    inDir_ = inDir;
    javaC_ = javaC;
    
  }
  public void jar(List<JarParam> args, List<String> params, String c_arg) throws IOException {

    String argString = buildArgs(args);
    List<String> all = new ArrayList<String>();
    all.add(javaC_);
    all.add(argString);
    all.addAll(params);
    if (args.contains(JarParam.C)) {
      all.add(JarParam.C.toString());
      all.add(c_arg);
      all.add(".");
    }
    try {
      String [] allArray = all.toArray(new String[all.size()]);
      if (ctx_.isLogEnabled(JavaJar.class)) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allArray.length; i++) {
          sb.append(allArray[i]);
          sb.append(" ");
        }
        ThreadLocalPrintStream.println("executing " + sb.toString());
      }
      Executor.executeProcess(new File(inDir_), allArray);
    } catch (InterruptedException e) {
      throw new IOException(e);
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
