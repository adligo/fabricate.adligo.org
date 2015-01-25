package org.adligo.fabricate.common.system;

import java.util.HashMap;
import java.util.Map;

public class CommandLineArgs {
  /**
   * This is printed out to the console 
   * so that the script knows there is a error
   * regardless of the language.
   */ 
  public static final String MESSAGE = "Message";
  /**
   * If passed in should return language underscore country;<br/>
   * (i.e. "en_US")
   */
  public static final String LOCALE = "locale";
  
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
        toRet.put(key.toLowerCase(), value);
      } else {
        toRet.put(arg.toLowerCase(),null);
      }
    }
    return toRet;
  }
}