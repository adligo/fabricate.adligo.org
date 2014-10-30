package org.adligo.fabricate.common;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {
  public static Map<String,String> parseArgs(String [] args) {
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
