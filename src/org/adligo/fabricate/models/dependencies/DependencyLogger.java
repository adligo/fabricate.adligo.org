package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;

public class DependencyLogger {
  private final I_FabSystem sys_;
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  
  public DependencyLogger(I_FabSystem system) {
    sys_ = system;
    constants_ = sys_.getConstants();
    sysMessages_ = constants_.getSystemMessages();
  }
  
  public void addDependencyLogMessage(I_Dependency dep, StringBuilder sb) {
    String artifact = dep.getArtifact();
    if (artifact == null) {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getArtifactColon(), artifact));
    } else {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getArtifactColon(), "'", artifact, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    
    String file = dep.getFileName();
    if (file == null) {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getFileNameColon(), file));
    } else {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getFileNameColon(), "'", file, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    String group = dep.getGroup();
    if (group == null) {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getGroupColon(), group));
    } else {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getGroupColon(), "'", group, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    String type = dep.getType();
    if (type == null) {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getTypeColon(), type));
    } else {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getTypeColon(), "'", type, "'"));
    }
    sb.append(sys_.lineSeparator());
    
    String version = dep.getVersion();
    if (version == null) {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getVersionColon(), version));
    } else {
      sb.append(FabLog.orderLine(constants_.isLeftToRight(), "\t", sysMessages_.getVersionColon(), "'", version, "'"));
    }
  }
}
