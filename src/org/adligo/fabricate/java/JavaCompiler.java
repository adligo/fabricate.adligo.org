package org.adligo.fabricate.java;

import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.system.Executor;
import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaCompiler {
  private I_FabSystem sys_;
  private String inDir_;
  private String javaC_;
  
  public JavaCompiler(I_FabSystem sys, String inDir, String javaC) {
    sys_ = sys;
    inDir_ = inDir;
    javaC_ = javaC;
    
  }
  public void compile(Map<JavaCParam,String> params, List<String> javaFiles) throws IOException {

    List<String> args = buildArgs(javaC_, params);
    args.addAll(javaFiles);
    I_Executor exe = sys_.getExecutor();
    I_ExecutionResult er = exe.executeProcess(inDir_, args.toArray(new String[args.size()]));
    if (er.getExitCode() != 0) {
      throw new IOException(er.getOutput());
    }
  }
  
  private List<String> buildArgs(String whichJavaC, Map<JavaCParam,String> params) {
    List<String> list = new  ArrayList<String>();
    list.add(whichJavaC);
    appendNoSpaceArg(params, JavaCParam.G, list);
    appendNoSpaceArg(params, JavaCParam.GC, list);
    appendNoSpaceArg(params, JavaCParam.NOWARN, list);
    appendNoSpaceArg(params, JavaCParam.VERBOSE, list);
    appendNoSpaceArg(params, JavaCParam.DEPRECATION, list);
    
    appendSpaceArg(params, JavaCParam.CP, list);
    /* this wasn't helping
    list.add(JavaCParam.SOURCEPATH.toString());
    list.add(srcPath);
    */
    appendNoSpaceArg(params, JavaCParam.PROC, list);
    appendSpaceArg(params, JavaCParam.PROCESSOR, list);
    appendSpaceArg(params, JavaCParam.PROCESSORPATH, list);
    
    appendSpaceArg(params, JavaCParam.D, list);
    appendSpaceArg(params, JavaCParam.S, list);
    appendSpaceArg(params, JavaCParam.H, list);
    
    appendNoSpaceArg(params, JavaCParam.IMPLICIT, list);
    appendSpaceArg(params, JavaCParam.ENCODING, list);
    appendSpaceArg(params, JavaCParam.SOURCE, list);
    appendSpaceArg(params, JavaCParam.TARGET, list);
    appendSpaceArg(params, JavaCParam.PROFILE, list);
    
    appendNoSpaceArg(params, JavaCParam.AKEY, list);
    appendNoSpaceArg(params, JavaCParam.J, list);
    appendNoSpaceArg(params, JavaCParam.WERROR, list);
    appendNoSpaceArg(params, JavaCParam.FILE, list);
    return list;
  }
  
  private void appendNoSpaceArg(Map<JavaCParam,String> params, 
      JavaCParam param, List<String> list) {
    if (params.containsKey(param)) {
      String value = params.get(param);
      if (StringUtils.isEmpty(value)) {
        list.add(param.toString());
      } else {
        list.add(param.toString() + value);
      }
    }
  }
  
  private void appendSpaceArg(Map<JavaCParam,String> params, 
      JavaCParam param, List<String> list) {
    if (params.containsKey(param)) {
      String value = params.get(param);
      if (StringUtils.isEmpty(value)) {
        list.add(param.toString());
      } else {
        list.add(param.toString());
        list.add(value);
      }
    }
  }
}