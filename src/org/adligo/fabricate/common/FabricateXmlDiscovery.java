package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.dev.v1_0.FabricateDevType;
import org.adligo.fabricate.xml_io.DevIO;

import java.io.File;
import java.io.IOException;

/**
 * this class attempts to locate a fabricate.xml
 * file either in the local directory of the script,
 * or if there is a project.xml file by 
 * @author scott
 *
 */
public class FabricateXmlDiscovery {
  private File fabricateXml;
  private File projectXml;
  
  public FabricateXmlDiscovery(boolean debug) {
    fabricateXml = new File("fabricate.xml");
    if (!fabricateXml.exists()) {
      projectXml = new File("project.xml");
      if (projectXml.exists()) {
        String projectXmlAbs = projectXml.getAbsolutePath();
        File dir = new File(projectXmlAbs.substring(0, projectXmlAbs.length() -12));
        File dirParent = dir.getParentFile();
        File devXml = new File(dirParent.getAbsolutePath() + File.separator + "dev.xml");
        if (!devXml.exists()) {
          File projectsDir = dirParent.getParentFile();
          fabricateXml = new File(projectsDir.getAbsolutePath() + File.separator + "fabricate.xml");
        } else {
          try {
           FabricateDevType dev = DevIO.parse(devXml);
           String projectGroup = dev.getProjectGroup();
           fabricateXml = new File(dirParent.getAbsolutePath() + File.separator + 
               projectGroup + File.separator + "fabricate.xml");
          } catch (IOException e) {
            if (debug) {
              ThreadLocalPrintStream.printTrace(e);
            }
          }
        }
      }
    }
  }
  
  public boolean hasFabricateXml() {
    if (fabricateXml == null) {
      return false;
    }
    if (fabricateXml.exists()) {
      return true;
    }
    return false;
  }

  public String getFabricateXmlPath() {
    return fabricateXml.getAbsolutePath();
  }

  public boolean hasProjectXml() {
    if (projectXml == null) {
      return false;
    }
    if (projectXml.exists()) {
      return true;
    }
    return false;
  }
  
  public String getProjectXml() {
    return projectXml.getAbsolutePath();
  }
}
