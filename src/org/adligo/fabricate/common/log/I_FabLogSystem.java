package org.adligo.fabricate.common.log;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;

public interface I_FabLogSystem {

  public I_FabLog getLog();

  /**
   * Stub for System.currentTimeMillis;
   * @return
   */
  public long getCurrentTime();
  
  /**
   * stub for Locale.getDefault().getLanguage()
   * @return
   */
  public String getDefaultLanguage();
  /**
   * stub for Locale.getDefault().getCountry()
   * @return
   */
  public String getDefaultCountry();
  
  /**
   * 
   * @return
   */
  public I_FabricateConstants getConstants();
}
