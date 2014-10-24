package org.adligo.fabricate;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Fabricate {
  private static PrintStream OUT = System.out;
  private static File fabricateXml;
  private static File projectXml;
  
  public static final void main(String [] args) {
    
    fabricateXml = new File("fabricate.xml");
    if (!fabricateXml.exists()) {
      projectXml = new File("project.xml");
      if (!projectXml.exists()) {
        OUT.println("Fabricate did not discover a fabricate.xml or project.xml.");
        return;
      }
    }
    OUT.println("Fabricateing...");
    
    Map<String,String> argMap = parseArgs(args);
    Set<Entry<String,String>> entries = argMap.entrySet();
    for (Entry<String,String> e: entries) {
      OUT.println(e.getKey() + " = " + e.getValue());
    }
    
    
  }
  
  private static Map<String,String> parseArgs(String [] args) {
    Map<String,String> toRet = new HashMap<String,String>();
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      int eq = arg.indexOf("=");
      if (eq != -1) {
        String key = arg.substring(0, eq);
        String value = "";
        if (arg.length() > eq + 1) {
          value = arg.substring(eq + 1, arg.length());
        } 
        toRet.put(key, value);
      } else {
        toRet.put(arg,null);
      }
    }
    return toRet;
  }
}
