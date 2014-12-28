package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.v1_0.FabricateType;
import org.adligo.fabricate.xml.io.v1_0.JavaType;

public class FabricateHelper {
  private FabricateType fabricate_;
  private JavaType java_;
  
  public FabricateHelper(FabricateType fab) {
    fabricate_ = fab;
    java_ = fab.getJava();
  }
  
  public String getXmx() {
    String xmx = FabricateDefaults.JAVA_XMX_DEFAULT;
    if (fabricate_ != null) {
      String jxmx = java_.getXmx();
      if (!StringUtils.isEmpty(jxmx)) {
        xmx = jxmx;
      }
    }
    return xmx;
  }
  
  public String getXms() {
    String xms = FabricateDefaults.JAVA_XMX_DEFAULT;
    String jxms = java_.getXms();
    if (!StringUtils.isEmpty(jxms)) {
      xms = jxms;
    }
    return xms;
  }
}
