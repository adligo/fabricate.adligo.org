package org.adligo.fabricate.common.i18n;

/**
 * This interface will hold all of the command line arguments
 * that can be passed to Fabricate (the fab or fab.bat script)
 * in a way that can be translated into other languages.
 * All methods in this interface take a boolean alias, when 
 * this alias is true, then the short version of the 
 * command line argument should be returned.  Aliases must
 * always start with a single '-' character and non aliases 
 * must always start with a two character String "--".<br/>
 * In Example;<br/>
 *   "-v" is short for "--version"<br/>
 * Also note this class works in conjunction with the 
 * CommandLineArgs class which allows for concatenation
 * of aliases;<br/>
 * In Example;<br/>
 *    fab -dr<br/>
 *    is the same as<br/>
 *    fab --development --rebuild-dependents<br/>
 *    is the same as<br/>
 *    fab -d -r<br/>
 * The fabricate java parts should always check for 
 * aliases only in the Map&lt;String,String&gt; of 
 * command line arguments, since the CommandLineArgs
 * class will make sure that non alias arguments will
 * be translated into alias arguments. <br/>
 *   This interface must only contain
 * common command line arguments for the general Fabricate
 * system, each command/stage or task plug-in may listen for 
 * additional arguments not defined here.  In order to prevent
 * overlap of command line arguments the following conventions 
 * must be followed;<br/>
 *    Only the Fabricate system may use the prefix Strings "-" and "--".<br/>
 *    Only the Fabricate system may use the Command argument key;<br/>
 *       (cmd in English)<br/>
 * Also as a final note Fabricate command line arguments should never 
 * be favored over xml key value nodes.  Command line arguments are generally
 * only used when a fork in the fabrication processing is necessary.
 * @author scott
 *
 */
public interface I_CommandLineConstants {

  /**
   * 
   * @param arg (i.e. --version)
   * @return the short alias of a command line argument
   * (i.e. -v).
   */
  public String getAlias(String arg);
  
  /**
   * This argument instructs Fabricate to run archive stages.
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-a"
   * alias == false
   *    "--archive"
   */
  public String getArchive(boolean alias);
  
  /**
   * This argument key instructs Fabricate to execute 
   * a command, the command itself 
   * @return The command line key for 
   * executing a command. In English this will be;<br/>
   * cmd<br/>
   * In Example;<br/>
   *    fab cmd=classpath2Eclipse<br/>
   *    fab cmd="encrypt" password="secretPassword"<br/>
   */
  public String getCommand();
  
  /**
   * This argument instructs Fabricate to confirm the 
   * local repository integrity.  For the first few versions
   * of Fabricate this will include; <br/>
   * 1) Verifying the md5 files contents
   * against the artifacts md5 check sum.
   * 2) Verifying the extracted artifacts directories
   * when compressed into a new zip file match the md5.
   * 
   * In far future versions of Fabricate this may 
   * also run some sort of virus scanning or other checks.
   * @param alias
   * @return
   */
  public String getConfirmRepositoryIntegrity(boolean alias);
  /**
   * This argument instructs Fabricate to use the parent directory of the 
   * project group directory (where fabricate.xml was found) as the projects
   * directory (where it will put the obtained projects).  When this 
   * argument is absent Fabricate will create a projects directory under
   * the project group directory for the projects.
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-d"
   * alias == false
   *    "--development"
   */
  public String getDevelopment(boolean alias);
  
  /**
   * This argument instructs Fabricate to turn on ALL LOGGING,
   * in the scripts and I_FabLog interface.
   * @param alias
   * @return or English this will be;
   * alias == true
   *     "-l"
   * alias == false
   *    "--log-verbosely"
   */
  public String getLog(boolean alias);
  
  /**
   * This argument instructs Fabricate to assume that the
   * ssh key-store password is empty for 'paswordless ssh'
   * when doing git clone, git push, when the protocol is ssh etc.
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-n"
   * alias == false
   *    "--no-ssh-keystore-passphrase"
   */
  public String getNoSshKeystorePassPhrase(boolean alias);
  
  /**
   * This comma delimited argument instructs Fabricate to build platforms,
   * other than the default 'JSE'.  Platforms are always compared ignoring
   * case, and the legacy platforms are 'JSE', 'GWT' and 'JME'.  The JSE platform
   * is always built first by default.<br/>
   * @return for English this will be;<br/>
   *  "platforms"<br/>
   * @return
   */
  public String getPlatforms();
  
  /**
   * This argument instructs Fabricate to delete the project group/projects directory
   * and all of it's contents when it is NOT running in development mode
   * and when the initial fab command was executed in the project group directory.
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-p"
   * alias == false
   *    "--purge"
   */
  public String getPurge(boolean alias);
  /**
   * This argument instructs Fabricate to rebuild all projects that dependent
   * on the project where the Fabricate program was started.  This applies to 
   * regular project dependency relationships as well as project group dependency
   * relationships.<br/>
   *   This argument is ignored when Fabricate is run from a project group directory
   * which has no dependencies on other project groups.
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-r"
   * alias == false
   *    "--rebuild-dependents"
   */
  public String getRebuildDependents(boolean alias);
  
  /**
   * This arguments turns off non optional stages.
   * @return
   */
  public String getSkip();
  /**
   * This argument turns on optional stages which are set as optional
   * in the fabricate.xml file.  In English this is 'stages' for command
   * line arguments like;<br/>
   * stages=submit2intelligence4j
   * stages= 
   * @return
   */
  public String getStages();
  /**
   * This argument should be checked for by any sort of
   * testing stage, so that the stage should be skipped
   * if it isn't set.
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-t"
   * alias == false
   *    "--test"
   */
  public String getTest(boolean alias);

  /**
   * This argument instructs Fabricate to update all projects from their various
   * source control management systems (git) before executing commands
   * or build stages. <This command line argument
   * is executed by the ObtainProjectsPresenter and 
   * does NOT end Fabrication. br/>
   * <br/>
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-u"
   * alias == false
   *    "--update"
   */
  public String getUpdate(boolean alias);
  /**
   * This argument instructs Fabricate to print 
   * out version information about it self. <br/>
   * In Example;<br/>
   * Version snapshot.<br/>
   * Compiled on 1/27/15 12:28:06 AM CST.<br/>
   * <br/>
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-v"
   * alias == false
   *    "--version"
   */
  public String getVersion(boolean alias);
  /**
   * This argument instructs Fabricate to write out 
   * out a log file to output/fab.log. <br/>
   * <br/>
   * @param alias
   * @return for English this will be;
   * alias == true
   *     "-w"
   * alias == false
   *    "--write-log"
   */  
  public String getWriteLog(boolean alias);
}
