package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.FabLog;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.LogSettingType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.LogSettingsType;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class FabSystemSetup {
  public static final Set<String> DEFALUT_ON_LOGS = getDefaultOns();
  
  private static Set<String> getDefaultOns() {
    Set<String> toRet = new HashSet<String>();
    
    toRet.add("org.adligo.fabricate.FabricateController");
    toRet.add("org.adligo.fabricate.repository.RepositoryManager");
    return Collections.unmodifiableSet(toRet);
  }
  
  @SuppressWarnings("boxing")
  public static Map<String, Boolean> getSettings(FabricateType fabricate, Set<String> defaultOns) {
    Map<String,Boolean> logSets = new HashMap<String,Boolean>();
    for (String clazz: defaultOns) {
      logSets.put(clazz, true);
    }
    LogSettingsType logs = fabricate.getLogs();
    if (logs != null) {
      List<LogSettingType> settings = logs.getLog();
      
      for (LogSettingType set: settings) {
        String clazz = set.getClazz();
        Boolean value = set.isSetting();
        if (value != null) {
          logSets.put(clazz, value);
        }
      }
    }
    
    return logSets;
  }
  
  public static void setup(FabSystem sys, String [] args) {
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    
    String language = sys.getDefaultLanguage();
    String country = sys.getDefaultCountry();
    
    String argLoc = argMap.get(CommandLineArgs.LOCALE);
    if (argLoc != null) {
      StringTokenizer st = new StringTokenizer(argLoc, "_");
      String tempLang = st.nextToken();
      String tempCountry = st.nextToken();
      language = tempLang;
      country = tempCountry;
    }
    I_FabricateConstants constants = sys.newFabConstantsDiscovery(language, country);
    
    if (!argMap.containsKey(CommandLineArgs.PASSABLE_ARGS_)) {
      String passableArgs = CommandLineArgs.toPassableString(args);
      argMap.put(CommandLineArgs.PASSABLE_ARGS_, passableArgs);
    }
    I_CommandLineConstants clConstants = constants.getCommandLineConstants();
    argMap = CommandLineArgs.normalizeArgs(argMap, clConstants);
    String logAlias = clConstants.getLog(true);
    boolean debug = argMap.containsKey(logAlias);
    Map<String, Boolean> em = Collections.emptyMap();
    FabLog log = new FabLog(em, debug);
    sys.setLog(log);
    sys.setConstants(constants);
    sys.setArgs(argMap);
  }
  
  public static void checkManditoryInjectedArgs(I_FabSystem sys) {
    String version = sys.getArgValue("java");
    if (StringUtils.isEmpty(version)) {
      I_FabricateConstants constants = sys.getConstants();
      I_FabLog log = sys.getLog();
      I_SystemMessages sysMessages = constants.getSystemMessages();
      log.println(CommandLineArgs.END);
      throw new IllegalStateException(sysMessages.getExceptionJavaVersionParameterExpected());
    }
    
    String start = sys.getArgValue("start");
    if (StringUtils.isEmpty(start)) {
      I_FabricateConstants constants = sys.getConstants();
      I_FabLog log = sys.getLog();
      I_SystemMessages sysMessages = constants.getSystemMessages();
      log.println(CommandLineArgs.END);
      throw new IllegalStateException(sysMessages.getExceptionNoStartTimeArg());
    }
  }
  
  public static void setup(FabSystem sys, FabricateType fabricate) {
    I_FabricateConstants constants = sys.getConstants();
    I_CommandLineConstants clConstants = constants.getCommandLineConstants();
    boolean debug = sys.hasArg(clConstants.getLog(true));
    
    Map<String, Boolean> logSets = getSettings(fabricate, DEFALUT_ON_LOGS);
    FabLog log = new FabLog(logSets, debug);
    sys.setLog(log);
    sys.setConstants(constants);
  }
}
