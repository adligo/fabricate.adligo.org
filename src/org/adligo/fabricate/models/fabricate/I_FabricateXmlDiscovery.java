package org.adligo.fabricate.models.fabricate;

public interface I_FabricateXmlDiscovery {
  /**
   * @return the absolute directory where fab was executed 
   * (with a project.xml) including the last
   * File.separatorChar.
   */
  public String getProjectXmlDir();
  
  /**
   * @return an absolute file name of fabricate.xml.
   */
  public String getFabricateXmlPath();
  
  /**
   * @return the absolute directory where fabricate.xml is including the last
   * File.separatorChar.
   */
  public String getFabricateXmlDir();
  /**
   * @return the absolute directory where dev.xml is including the last
   * File.separatorChar.
   */
  public String getDevXmlDir();

  /**
   * This is the location of the projects including the last File.separatorChar.
   * @return
   */
  public String getProjectsDir();
}