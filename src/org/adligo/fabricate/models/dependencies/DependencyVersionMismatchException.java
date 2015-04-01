package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;

public class DependencyVersionMismatchException extends Exception {
  public static void logProjectError(I_FabSystem sys, String fabricateOrProjectFile, 
      DependencyVersionMismatchException e) {
    
    I_FabricateConstants constants =  sys.getConstants();
    I_SystemMessages messages = constants.getSystemMessages();
    String message = messages.getTheFollowingDependenciesVersionDoesNotMatchTheFileName();
    StringBuilder sb = new StringBuilder();
    sb.append(message);
    sb.append(sys.lineSeparator());
    sb.append(fabricateOrProjectFile);
    sb.append(sys.lineSeparator());
    
    DependencyLogger logger = new DependencyLogger(sys);
    I_Dependency dep = e.getDependency();
    logger.addDependencyLogMessage(dep, sb);
    I_FabLog log = sys.getLog();
    log.println(sb.toString());
  }
  
  private static final long serialVersionUID = 1L;
  private I_Dependency dep_;
  
  public DependencyVersionMismatchException(I_Dependency dep) {
    dep_ = dep;
  }

  public I_Dependency getDependency() {
    return dep_;
  }

}
