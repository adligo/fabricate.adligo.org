package org.adligo.fabricate.models.fabricate;

import org.adligo.fabricate.common.system.FabricateDefaults;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;

public class JavaSettings implements I_JavaSettings {
  private final String xms_;
  private final String xmx_;
  private final int threads_;

  public JavaSettings(I_JavaSettings other) {
    threads_ = other.getThreads();
    xms_ = other.getXms();
    xmx_ = other.getXmx();
  }
  
  public JavaSettings(JavaType java) {
    this(new JavaSettingsMutant(java));
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_JavaSettings#getThreads()
   */
  @Override
  public int getThreads() {
    if (threads_ == 0) {
      return FabricateDefaults.JAVA_THREADS;
    }
    return threads_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.fabricate.I_JavaSettings#getXmx()
   */
  @Override
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

}
