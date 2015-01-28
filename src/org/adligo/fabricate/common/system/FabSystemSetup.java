package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.FabConstantsDiscovery;
import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.DelayedLog;
import org.adligo.fabricate.common.log.FabLog;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.LogSettingType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.LogSettingsType;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FabSystemSetup {

  public static Map<String, Boolean> getSettings(FabricateType fabricate) {
    Map<String,Boolean> logSets = new HashMap<String,Boolean>();
    LogSettingsType logs = fabricate.getLogs();
    List<LogSettingType> settings = logs.getLog();
    for (LogSettingType set: settings) {
      String clazz = set.getClazz();
      Boolean value = set.isSetting();
      logSets.put(clazz, value);
    }
    return logSets;
  }
  
  public static void setupWithDelayedLog(FabSystem sys, String [] args) {
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
    I_FabricateConstants constants = null;
    try {
      constants = new FabConstantsDiscovery(language, country);
    } catch (IOException x) {
      constants = FabricateEnConstants.INSTANCE;
    }
    I_CommandLineConstants clConstants = constants.getCommandLineConstants();
    argMap = CommandLineArgs.normalizeArgs(argMap, clConstants);
    String logAlias = clConstants.getLog(true);
    boolean debug = argMap.containsKey(logAlias);
    DelayedLog log = new DelayedLog(Collections.emptyMap(), debug);
    sys.setLog(log);
    sys.setConstants(constants);
    sys.setArgs(argMap);
  }
  
  public static void setupWithDelayedLog(FabSystem sys, FabricateType fabricate) {
    I_FabricateConstants constants = sys.getConstants();
    I_CommandLineConstants clConstants = constants.getCommandLineConstants();
    boolean debug = sys.hasArg(clConstants.getLog(true));
    
    Map<String, Boolean> logSets = getSettings(fabricate);
    DelayedLog log = new DelayedLog(logSets, debug);
    sys.setLog(log);
    sys.setConstants(constants);
  }
  
  public static void setupWithRegularLog(FabSystem sys, FabricateType fabricate) {
    I_FabricateConstants constants = sys.getConstants();
    I_CommandLineConstants clConstants = constants.getCommandLineConstants();
    boolean debug = sys.hasArg(clConstants.getLog(true));
    
    Map<String, Boolean> logSets = getSettings(fabricate);
    FabLog log = new FabLog(logSets, debug);
    sys.setLog(log);
    sys.setConstants(constants);
  }
}
