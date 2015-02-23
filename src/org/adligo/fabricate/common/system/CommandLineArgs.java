package org.adligo.fabricate.common.system;


import org.adligo.fabricate.common.i18n.I_CommandLineConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class CommandLineArgs {
  /**
   * A special key for passing the original fab command
   * from the command line to the FabricateController
   */
  public static final String PASSABLE_ARGS_ = "passableArgs";
  
  private static final String PASSABLE_ARGS_START = "\"[";
  /**
   * This is printed out to the console 
   * so that the script knows that the last
   * line of one of the setup (args or opts)
   * programs has completed.  This was added 
   * for the fab shell (sh) script, which needed
   * to read the output line by line while the 
   * setup programs run so that the log will 
   * show up correctly on the command line by 
   * echoing to the terminals console while the
   * setup programs run concurrently.
   */ 
  public static final String LASTLINE = "LASTLINE ";
  /**
   * This is printed out to the console 
   * so that the script knows there is a error
   * regardless of the language, so that it ends the
   * run of the script.
   */ 
  public static final String END = LASTLINE + "END";
  /**
   * This is a optional command line argument which allows
   * the user of Fabricate to override the default locale of the JVM.
   * It must be set to language code underscore country code,
   * and will default to United States English when there is
   * no implementation of the specified locale;<br/>
   * In Example;<br/>
   *   fab --=en_US<br/>
   *   fab --=fr_CA<br/>
   *   fab --=en_CA<br/>
   *   fab --=fr_FR<br/>
   */
  public static final String LOCALE = "--";
  
  public static String toPassableString(String [] args) {
    StringBuilder sb = new StringBuilder();
    sb.append(PASSABLE_ARGS_START);
    int len = args.length;
    sb.append(len);
    sb.append(",");
    
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      int tokenLen = arg.length();
      sb.append("[");
      sb.append(tokenLen);
      sb.append(",");
      sb.append(arg);
      sb.append("]");
    }
    sb.append("]\"");
    return sb.toString();
  }
  
  @SuppressWarnings("boxing")
  public static String[] fromPassableString(String in) {
    if (in.length() < PASSABLE_ARGS_START.length() + 3) {
      return new String[] {};
    }
    
    int firstNumEnd = in.indexOf(",");
    int firstBlock = in.indexOf("[");
    String firstNum =  in.substring(firstBlock + 1, firstNumEnd);
    Integer tokens = new Integer(firstNum);
    
    String []  result = new String[tokens];
    if (tokens == 0) {
      return result;
    }
    
    int last = firstNumEnd + firstNum.length();
    String remaining = in.substring(last,  in.length());
    
    for (int i = 0; i < tokens; i++) {
      int nextComma = remaining.indexOf(",");
      if (nextComma == -1) {
        break;
      }
      String nextNum =  remaining.substring(1, nextComma);
      
      int afterComma = nextComma + 1;
      Integer num = new Integer(nextNum);
      last = afterComma + num;
      String next =  remaining.substring(afterComma, last);
      result[i] = next;
      remaining = remaining.substring(last + 1, remaining.length());
    }
    return result;
  }
  
  public static Map<String,String> parseArgs(String [] args) {
    Map<String,String> toRet = new TreeMap<String,String>();
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
        boolean added = false;
        if (arg.indexOf("--") == 0) {
          toRet.put(arg, null);
          added = true;
        } else if (arg.indexOf("-") == 0) {
          char [] chars = arg.toCharArray();
          for (int j = 0; j < chars.length; j++) {
            char c = chars[j];
            if (c != '-') {
              toRet.put("-" + c,null);
              added = true;
            }
          }
        }
        if (!added) {
          toRet.put(arg,null);
        }
      }
    }
    return toRet;
  }
  public static Map<String,String> normalizeArgs(Map<String,String> args, I_CommandLineConstants constants) {
    Set<Entry<String,String>> entries = new HashSet<Entry<String,String>>(args.entrySet());
    
    for (Entry<String,String> e: entries) {
      String key = e.getKey();
      String value = e.getValue();
      if (key.indexOf("--") == 0 && !LOCALE.equals(key)) {
        String alias = constants.getAlias(key);
        if (alias != null) {
          //replace the value with the alias
          args.remove(key);
          args.put(alias, value);
        }
      }
    }
    return Collections.unmodifiableMap(args);
  }
  
  public static void appendArgs(StringBuilder sb, Map<String,String> args) {
    Set<Entry<String,String>> entries = args.entrySet();
    for (Entry<String,String> e: entries) {
      String key = e.getKey();
      String value = e.getValue();
      sb.append(" ");
      if (value != null) {
        sb.append(key);
        sb.append("=");
        sb.append(value);
      } else {
        sb.append(key);
      }
    }
  }
}
