package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.ProjectType;

import java.util.Map;

/**
 * I_FabSetupTask is a interface to allow plug-able
 * tasks to be called from fabricate.  It is generally
 * assumed that I_FabSetupTask's are NOT concurrent
 * and only one at a time will run on the fabricate
 * environment.
 * 
 * @author scott
 *
 */
public interface I_FabSetupTask {
  public void setInitalDirPath(String initalDir);
  public void setFabricate(FabricateType fabricate);
  public void setProject(ProjectType project);
  /**
   * 
   * @param args the parsed arguments from the initial call to java
   * @return
   */
  public I_FabContext setup(Map<String,String> args);
}
