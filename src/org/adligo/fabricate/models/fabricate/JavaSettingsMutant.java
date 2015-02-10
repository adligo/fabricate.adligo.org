package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;

public class JavaSettingsMutant implements I_JavaSettings {
  private String xms_;
  private String xmx_;
  private int threads_;

  public JavaSettingsMutant() {}
  
  public JavaSettingsMutant(I_JavaSettings other) {
    threads_ = other.getThreads();
    xms_ = other.getXms();
    xmx_ = other.getXmx();
  }
  
  public JavaSettingsMutant(JavaType java) {
    threads_ = getThreads(java);
    xms_ = getXms(java);
    xmx_ = getXmx(java);
  }

  public String getXms() {
    if (xms_ == null) {
      return FabricateDefaults.JAVA_XMS_DEFAULT;
    }
    return xms_;
  }

  public String getXmx() {
    if (xmx_ == null) {
      return FabricateDefaults.JAVA_XMX_DEFAULT;
    }
    return xmx_;
  }

  public int getThreads() {
    if (threads_ == 0) {
      return FabricateDefaults.JAVA_THREADS;
    }
    return threads_;
  }

  public void setXms(String xms) {
    this.xms_ = xms;
  }

  public void setXmx(String xmx) {
    this.xmx_ = xmx;
  }

  public void setThreads(int threads) {
    this.threads_ = threads;
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
