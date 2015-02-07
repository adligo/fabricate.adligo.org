package org.adligo.fabricate.models.fabricate;

public interface I_FabricateXmlDiscovery {

  public String getProjectXmlDir();
  
  /**
   * @return an absolute file name of fabricate.xml.
   */
  public String getFabricateXmlPath();
  
  public String getFabricateXmlDir();

  public String getDevXmlDir();

}