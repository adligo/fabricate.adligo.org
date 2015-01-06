package org.adligo.fabricate.common;

import org.adligo.fabricate.files.FabFiles;
import org.adligo.fabricate.files.I_FabFiles;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;

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


  private I_FabFiles files_ = FabFiles.INSTANCE;
  private File fabricateXml_;
  private File projectXml_;
  
  public FabricateXmlDiscovery(I_FabFiles stubs, boolean debug) {
    files_ = stubs;
    if (files_.exists("fabricate.xml")) {
      fabricateXml_ = new File("fabricate.xml");
    } else {
      if (files_.exists("project.xml")) {
        String projectXmlAbs = new File("project.xml").getAbsolutePath();
        File dir = new File(projectXmlAbs.substring(0, projectXmlAbs.length() -12));
        File dirParent = dir.getParentFile();
        String devXmlFilePath = dirParent.getAbsolutePath() + File.separator + "dev.xml";
        
        if (!files_.exists(devXmlFilePath)) {
          File projectsDir = dirParent.getParentFile();
          fabricateXml_ = new File(projectsDir.getAbsolutePath() + File.separator + "fabricate.xml");
        } else {
          try {
           FabricateDevType dev = files_.parseDev_v1_0(devXmlFilePath);
           String projectGroup = dev.getProjectGroup();
           fabricateXml_ = new File(dirParent.getAbsolutePath() + File.separator + 
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
    if (fabricateXml_ == null) {
      return false;
    }
    if (fabricateXml_.exists()) {
      return true;
    }
    return false;
  }

  public String getFabricateXmlPath() {
    return fabricateXml_.getAbsolutePath();
  }

  public boolean hasProjectXml() {
    if (projectXml_ == null) {
      return false;
    }
    if (projectXml_.exists()) {
      return true;
    }
    return false;
  }
  
  public String getProjectXml() {
    return projectXml_.getAbsolutePath();
  }
  
  public I_FabFiles getFiles() {
    return files_;
  }
  
}
