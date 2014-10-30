package org.adligo.fabricate.external;

import org.adligo.fabricate.common.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaCompilerCalls {
  private String inDir_;
  private String javaC_;
  
  public JavaCompilerCalls(String inDir, String javaC) {
    inDir_ = inDir;
    javaC_ = javaC;
    
  }
  public void compile(String srcPath, Map<JavaCParam,String> params, List<String> javaFiles) throws IOException {

    List<String> args = buildArgs(javaC_, srcPath, params);
    args.addAll(javaFiles);
    try {
      Executor.executeProcess(new File(inDir_), args.toArray(new String[args.size()]));
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
  
  private List<String> buildArgs(String whichJavaC, String srcPath, Map<JavaCParam,String> params) {
    List<String> list = new  ArrayList<String>();
    list.add(whichJavaC);
    appendNoSpaceArg(params, JavaCParam.G, list);
    appendNoSpaceArg(params, JavaCParam.GC, list);
    appendNoSpaceArg(params, JavaCParam.NOWARN, list);
    appendNoSpaceArg(params, JavaCParam.VERBOSE, list);
    appendNoSpaceArg(params, JavaCParam.DEPRECATION, list);
    
    appendSpaceArg(params, JavaCParam.CP, list);
    list.add(JavaCParam.SOURCEPATH.toString());
    list.add(srcPath);
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
