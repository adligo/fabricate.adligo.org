package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandLineEnConstants implements I_CommandLineConstants {
  public static final CommandLineEnConstants INSTANCE = new CommandLineEnConstants();
  private static final String A = "-a";
  private static final String ARCHIVE = "--archive";
  /**
   * key to a comma delimited String which turns into a List<String>
   */
  private static final String COMMAND = "cmd";
  private static final String C = "-c";
  private static final String CONFIRM = "--confirm-repository-integrity";
  private static final String D = "-d";
  private static final String DEVELOPMENT = "--development";
  private static final String G = "-g";
  private static final String GROW_BRANCHES = "--grow-branches";
  private static final String L = "-l";
  private static final String LOG = "--log-verbosely";
  private static final String M = "-m";
  private static final String MARK_VERSIONS = "--mark-versions";
  private static final String R = "-r";
  private static final String REBUILD = "--rebuild-dependents";
  
  private static final String S = "-s";
  private static final String SHARE = "--share";
  /**
   * key to a comma delimited String which turns into a  key to a List<String>
   */
  private static final String STAGES = "stages";
  private static final String T = "-t";
  private static final String TEST = "--test";
  
  private static final String U = "-u";
  private static final String UPDATE = "--update";
  private static final String V = "-v";
  private static final String VERSION = "--version";
  private Map<String,String> map_;
  
  private CommandLineEnConstants() {
    map_ = new HashMap<String, String>();
    map_.put(ARCHIVE, A);
    map_.put(CONFIRM, C);
    map_.put(DEVELOPMENT, D);
    map_.put(GROW_BRANCHES, G);
    map_.put(LOG, L);
    map_.put(MARK_VERSIONS, M);
    map_.put(REBUILD, R);
    map_.put(SHARE, S);
    map_.put(TEST, T);
    map_.put(UPDATE, U);
    map_.put(VERSION, V);
    map_ = Collections.unmodifiableMap(map_);
  }
  
  @Override
  public String getAlias(String arg) {
    return map_.get(arg);
  }
  
  @Override
  public String getArchive(boolean alias) {
    if (alias) {
      return A;
    }
    return ARCHIVE;
  }
  
  @Override
  public String getCommand() {
    return COMMAND;
  }

  @Override
  public String getConfirmRepositoryIntegrity(boolean alias) {
    if (alias) {
      return C;
    }
    return CONFIRM;
  }
  
  @Override
  public String getDevelopment(boolean alias) {
    if (alias) {
      return D;
    }
    return DEVELOPMENT;
  }

  @Override
  public String getGrowBranches(boolean alias) {
    if (alias) {
      return G;
    }
    return GROW_BRANCHES;
  }
  
  @Override
  public String getLog(boolean alias) {
    if (alias) {
      return L;
    }
    return LOG;
  }
  
  @Override
  public String getShare(boolean alias) {
    if (alias) {
      return S;
    }
    return SHARE;
  }
  
  @Override
  public String getStages() {
    return STAGES;
  }
  
  @Override
  public String getTest(boolean alias) {
    if (alias) {
      return T;
    }
    return TEST;
  }
  
  @Override
  public String getMarkVersions(boolean alias) {
    if (alias) {
      return M;
    }
    return MARK_VERSIONS;
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
