package org.adligo.fabricate.external;

public enum JarParam {
  c("-c"),t("-t"),x("-x"),u("-u"),//no verbose it looks for the JavaJar log to be on
  f("-f"), m("-m"), n("-n"), e("-e"),
  Zero("-0"), M("-M"), i("-i"), C("-C"); 
  private String id_;
  
  private JarParam(String id) {
    id_ = id;
  }
  
  public String toString() {
    return id_;
  }
  
  public String toArg() {
    return id_.substring(1, 2);
  }
}
