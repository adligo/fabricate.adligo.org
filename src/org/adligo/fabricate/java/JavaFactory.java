package org.adligo.fabricate.java;

import org.adligo.fabricate.common.system.I_FabSystem;

public class JavaFactory {

  public JavaCompiler newJavaCompiler(I_FabSystem sys,String inDir,String javaC) {
    return new JavaCompiler(sys, inDir, javaC);
  }
  
  public JavaJar newJavaJar(I_FabSystem sys,String inDir,String jarPath) {
    return new JavaJar(sys, inDir, jarPath);
  }
  
  public ManifestParser newManifestParser() {
    return new ManifestParser();
  }
}
