package org.adligo.fabricate.models.fabricate;

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

}
