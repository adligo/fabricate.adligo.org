package org.adligo.fabricate.common;

import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.files.FabFileIO;
import org.adligo.fabricate.files.I_FabFileIO;
import org.adligo.fabricate.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.files.xml_io.I_FabXmlFileIO;
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


  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private File fabricateXml_;
  private File projectXml_;
  
  public FabricateXmlDiscovery(I_FabFileIO files, I_FabXmlFileIO xmlFiles, boolean debug) {
    if (files != null) {
      files_ = files;
    } else {
      files_ = FabFileIO.INSTANCE;
    }
    if (xmlFiles != null) {
      xmlFiles_ = xmlFiles;
    } else {
      xmlFiles_ = FabXmlFileIO.INSTANCE;
    }
    setup(debug);
  }
  
  public FabricateXmlDiscovery(boolean debug) {
    this(null, null, debug);
  }
  
  public String getFabricateXmlPath() {
    return fabricateXml_.getAbsolutePath();
  }


  public String getProjectXml() {
    return projectXml_.getAbsolutePath();
  }
  
  public I_FabFileIO getFiles() {
    return files_;
  }
  

  public I_FabXmlFileIO getXmlFiles() {
    return xmlFiles_;
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
  
  public boolean hasProjectXml() {
    if (projectXml_ == null) {
      return false;
    }
    if (projectXml_.exists()) {
      return true;
    }
    return false;
  }
  
  
  private void setup(boolean debug) {
    
    if (files_.exists("fabricate.xml")) {
      fabricateXml_ = files_.instance("fabricate.xml");
    } else {
      if (files_.exists("project.xml")) {
        String projectXmlAbs = files_.instance("project.xml").getAbsolutePath();
        File dir = files_.instance(projectXmlAbs.substring(0, projectXmlAbs.length() -12));
        File dirParent = dir.getParentFile();
        String devXmlFilePath = dirParent.getAbsolutePath() + File.separator + "dev.xml";
        
        if (!files_.exists(devXmlFilePath)) {
          File projectsDir = dirParent.getParentFile();
          fabricateXml_ = files_.instance(projectsDir.getAbsolutePath() + File.separator + "fabricate.xml");
        } else {
          try {
           FabricateDevType dev = xmlFiles_.parseDev_v1_0(devXmlFilePath);
           String projectGroup = dev.getProjectGroup();
           fabricateXml_ = files_.instance(dirParent.getAbsolutePath() + File.separator + 
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
}
