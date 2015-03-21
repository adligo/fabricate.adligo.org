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

  private static final String L = "-l";
  private static final String LOG = "--log-verbosely";
  private static final String N = "-n";
  private static final String NO_SSH_PASSPHRASE = "--no-ssh-keystore-passphrase";
  private static final String PLATFORMS = "platforms";

  private static final String P = "-p";
  private static final String PURGE = "--purge";
  private static final String R = "-r";
  private static final String REBUILD = "--rebuild-dependents";
  
  /**
   * key to a comma delimited String which turns into a  key to a List<String>
   * and represents non optional stages to skip (not execute).
   */
  private static final String SKIP = "skip";
  /**
   * key to a comma delimited String which turns into a  key to a List<String>
   * and represents optional stages to execute (in fabricate.xml order).
   */
  private static final String STAGES = "stages";
  private static final String T = "-t";
  private static final String TEST = "--test";
  
  private static final String U = "-u";
  private static final String UPDATE = "--update";
  private static final String V = "-v";
  private static final String VERSION = "--version";
  private static final String W = "-w";
  private static final String WRITE_LOG = "--write-log";
  
  private Map<String,String> map_;
  
  private CommandLineEnConstants() {
    map_ = new HashMap<String, String>();
    map_.put(ARCHIVE, A);
    map_.put(CONFIRM, C);
    map_.put(DEVELOPMENT, D);
    map_.put(LOG, L);
    map_.put(NO_SSH_PASSPHRASE, N);
    map_.put(PURGE, P);
    map_.put(REBUILD, R);
    map_.put(TEST, T);
    map_.put(UPDATE, U);
    map_.put(VERSION, V);
    map_.put(WRITE_LOG, W);
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
  public String getLog(boolean alias) {
    if (alias) {
      return L;
    }
    return LOG;
  }

  @Override
  public String getSkip() {
    return SKIP;
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
  public String getNoSshKeystorePassPhrase(boolean alias) {
    if (alias) {
      return N;
    }
    return NO_SSH_PASSPHRASE;
  }
  
  @Override
  public String getPlatforms() {
    return PLATFORMS;
  }
  
  @Override
  public String getPurge(boolean alias) {
    if (alias) {
      return P;
    }
    return PURGE;
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
  
  @Override
  public String getWriteLog(boolean alias) {
    if (alias) {
      return W;
    }
    return WRITE_LOG;
  }
}
