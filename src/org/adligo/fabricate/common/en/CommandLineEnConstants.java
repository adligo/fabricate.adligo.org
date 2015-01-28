package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandLineEnConstants implements I_CommandLineConstants {
  public static final CommandLineEnConstants INSTANCE = new CommandLineEnConstants();
  private static final String COMMAND = "cmd";
  private static final String D = "-d";
  private static final String DEVELOPMENT = "--development";
  private static final String L = "-l";
  private static final String LOG = "--log-verbosely";
  private static final String R = "-r";
  private static final String REBUILD = "--rebuild-dependents";
  private static final String U = "-u";
  private static final String UPDATE = "--update";
  private static final String V = "-v";
  private static final String VERSION = "--version";
  private Map<String,String> map_;
  
  private CommandLineEnConstants() {
    map_ = new HashMap<String, String>();
    map_.put(DEVELOPMENT, D);
    map_.put(LOG, L);
    map_.put(REBUILD, R);
    map_.put(UPDATE, U);
    map_.put(VERSION, V);
    map_ = Collections.unmodifiableMap(map_);
  }
  
  @Override
  public String getAlias(String arg) {
    return map_.get(arg);
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }

  @Override
  public String getDevelopment(boolean alias) {
    if (alias) {
      return D;
    }
    return DEVELOPMENT;
  }



  @Override
  public String getLog(boolean alias) {
    if (alias) {
      return L;
    }
    return LOG;
  }
  
  @Override
  public String getRebuildDependents(boolean alias) {
    if (alias) {
      return R;
    }
    return REBUILD;
  }

  @Override
  public String getUpdate(boolean alias) {
    if (alias) {
      return U;
    }
    return UPDATE;
  }

  @Override
  public String getVersion(boolean alias) {
    if (alias) {
      return V;
    }
    return VERSION;
  }
}
