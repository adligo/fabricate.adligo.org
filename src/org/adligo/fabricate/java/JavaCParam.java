package org.adligo.fabricate.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum JavaCParam {
  G("-g"), GC("-g:"), NOWARN("-nowarn"), VERBOSE("-verbose"),
  DEPRECATION("-deprecation"), CP("-cp"), SOURCEPATH("-sourcepath"),
  //skip -bootclasspath, extdirs, -endorseddirs
  PROC("-proc:"), PROCESSOR("-processor:"), PROCESSORPATH("-processorpath"),
  PARAMETERS("-parameters"), D("-d"), S("-s"), H("-h"),
  IMPLICIT("-implicit:"), ENCODING("-encoding"), SOURCE("-source"),
  TARGET("-target"), PROFILE("-profile"),AKEY("-Akey"),
  J("-j"),WERROR("-Werror"),FILE("@");
  private static final Map<String,JavaCParam> ID_LOOKUP = getIdLookup();
  
  private String id_;
  
  private JavaCParam(String id) {
    id_ = id;
  }
  
  public String toString() {
    return id_;
  }
  
  public static JavaCParam get(String name) {
    return ID_LOOKUP.get(name);
  }
  
  private static final Map<String,JavaCParam> getIdLookup() {
    Map<String,JavaCParam> toRet = new HashMap<String, JavaCParam>();
    JavaCParam [] params = JavaCParam.values();
    for (int i = 0; i < params.length; i++) {
      JavaCParam param = params[i];
      add(param, toRet);
    }
    return Collections.unmodifiableMap(toRet);
  }
  
  private static void add(JavaCParam p, Map<String,JavaCParam> map) {
    map.put(p.toString(), p);
  }
}
