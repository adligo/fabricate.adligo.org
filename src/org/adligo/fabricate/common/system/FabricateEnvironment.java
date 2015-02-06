package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.util.StringUtils;

/**
 * This class holds environment variable key/names
 * constants.
 * @author scott
 *
 */
public class FabricateEnvironment {
  /**
   * This required environment variable 
   * tells the Fabricate scripts (fab, fab.bat)
   * where to find the fabricate_*.jar 
   */
  public static final String FABRICATE_HOME = "FABRICATE_HOME";
  /**
   * This optional environment variable 
   * tells fabricate where to find/put the local repository.
   * When it is NOT present the local repository is placed
   * in ${USER_HOME}/local_repository.
   */
  public static final String FABRICATE_REPOSITORY = "FABRICATE_REPOSITORY";
  public static final FabricateEnvironment INSTANCE = new FabricateEnvironment();
  /**
   * This required environment variable helps fabricate
   * find java tools, like jar, compile, java etc.
   */
  public static final String JAVA_HOME = "JAVA_HOME";
  
  private FabricateEnvironment() {}
  
  /**
   * This method either returns a non empty value or throws a exception.
   * @param sys
   * @return
   * @throws IllegalStateException
   */
  public String getJavaHome(I_FabSystem sys) throws IllegalStateException {
    String home = sys.getenv(FabricateEnvironment.JAVA_HOME);
    if (StringUtils.isEmpty(home)) {
      I_FabricateConstants constants = sys.getConstants();
      I_SystemMessages sysMessages = constants.getSystemMessages();
      String message = sysMessages.getExceptionNoJavaHomeSet();
      I_FabLog log = sys.getLog();
      log.println(message);
      log.println(CommandLineArgs.END);
      throw new IllegalStateException();
    }
    return home;
  }
  
  /**
   * This method either returns a non empty value or throws a exception.
   * @param sys
   * @return
   * @throws IllegalStateException
   */
  public String getFabricateHome(I_FabSystem sys) throws IllegalStateException {
    String home = sys.getenv(FabricateEnvironment.FABRICATE_HOME);
    if (StringUtils.isEmpty(home)) {
      I_FabricateConstants constants = sys.getConstants();
      I_SystemMessages sysMessages = constants.getSystemMessages();
      String message = sysMessages.getExceptionNoFabricateHomeSet();
      I_FabLog log = sys.getLog();
      log.println(message);
      log.println(CommandLineArgs.END);
      throw new IllegalStateException();
    }
    return home;
  }
  
  public String getFabricateRepository(I_FabSystem sys) throws IllegalStateException {
    String repo = sys.getenv(FabricateEnvironment.FABRICATE_REPOSITORY);
    if (StringUtils.isEmpty(repo)) {
      repo = FabricateDefaults.LOCAL_REPOSITORY;
    }
    return repo;
  }
}
