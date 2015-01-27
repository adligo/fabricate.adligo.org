package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLogSystem;


public interface I_FabSystem extends I_FabLogSystem {
  public boolean isDebug();
  public I_FabFileIO getFileIO();
  public I_FabXmlFileIO getXmlFileIO();
  /**
   * Stub for System.getenv(String key);
   * @param key
   * @return
   */
  public String getenv(String key);
  
  /**
   * Stub for System.lineSeperator()
   * This method should be preferred over the one in I_FabricateConstants.
   * @return
   */
  public String lineSeperator();
}
