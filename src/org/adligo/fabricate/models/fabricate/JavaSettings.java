package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.FabricateDefaults;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;

public class JavaSettings implements I_JavaSettings {
  private final String xms_;
  private final String xmx_;
  private final int threads_;
  
  public JavaSettings(JavaType java) {
    threads_ = getThreads(java);
    xms_ = getXms(java);
    xmx_ = getXmx(java);
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_JavaSettings#getThreads()
   */
  @Override
  public int getThreads() {
    return threads_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_JavaSettings#getXmx()
   */
  @Override
  public String getXmx() {
    return xmx_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_JavaSettings#getXms()
   */
  @Override
  public String getXms() {
    return xms_;
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
