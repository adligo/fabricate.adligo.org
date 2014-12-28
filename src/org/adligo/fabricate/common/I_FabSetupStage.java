package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.project.v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io.v1_0.FabricateType;

import java.util.Map;

/**
 * I_FabSetupTask is a interface to allow plug-able
 * tasks to be called from fabricate.  It is generally
 * assumed that I_FabSetupTask's are NOT concurrent
 * and only one at a time will run on the fabricate
 * environment.
 *   Note fabricate generally uses system specific paths
 * using File.seperator.
 * 
 * @author scott
 *
 */
public interface I_FabSetupStage {
  public void setInitalDirPath(String initalDir);
  public void setFabricate(FabricateType fabricate);
  public void setProject(FabricateProjectType project);
  public void setFabricateXmlPath(String fabricateXmlPath);
  public void setProjectXmlPath(String projectXmlPath);
  /**
   * 
   * @param args the parsed arguments from the initial call to java
   * @return
   */
  public I_FabContext setup(Map<String,String> args);
}
