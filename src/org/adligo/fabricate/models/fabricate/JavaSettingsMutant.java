package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;

public class JavaSettingsMutant implements I_JavaSettings {
  private String xms;
  private String xmx;
  private int threads;

  public JavaSettingsMutant() {}
  
  public JavaSettingsMutant(I_JavaSettings other) {
    threads = other.getThreads();
    xms = other.getXms();
    xmx = other.getXmx();
  }
  
  public JavaSettingsMutant(JavaType java) {
    threads = getThreads(java);
    xms = getXms(java);
    xmx = getXmx(java);
  }

  public String getXms() {
    return xms;
  }

  public String getXmx() {
    return xmx;
  }

  public int getThreads() {
    return threads;
  }

  public void setXms(String xms) {
    this.xms = xms;
  }

  public void setXmx(String xmx) {
    this.xmx = xmx;
  }

  public void setThreads(int threads) {
    this.threads = threads;
  }
  
  @SuppressWarnings("boxing")
  private int getThreads(JavaType java) {
    int threads = FabricateDefaults.JAVA_THREADS;
    if (java != null) {
      Integer threadsFromFile = java.getThreads();
      if (threadsFromFile != null) {
        threads = threadsFromFile;
      }
    }
    return threads;
  }
  
  private String getXmx(JavaType java) {
    String xmx = FabricateDefaults.JAVA_XMX_DEFAULT;
    if (java != null) {
      String jxmx = java.getXmx();
      if (!StringUtils.isEmpty(jxmx)) {
        xmx = jxmx;
      }
    }
    return xmx;
  }
  
  private String getXms(JavaType java) {
    String xms = FabricateDefaults.JAVA_XMS_DEFAULT;
    if (java != null) {
      String jxms = java.getXms();
      if (!StringUtils.isEmpty(jxms)) {
        xms = jxms;
      }
    }
    return xms;
  }
}
