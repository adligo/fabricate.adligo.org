package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.FabFileUtils;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.models.fabricate.I_FabricateXmlDiscovery;
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
public class FabricateXmlDiscovery implements I_FabricateXmlDiscovery {

  private final I_FabSystem sys_;
  private final I_CommandLineConstants clConstants_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private File fabricateXml_;
  private File projectXml_;
  private File devXml_;
  private boolean devParseException_ = false;
  
  public FabricateXmlDiscovery(I_FabSystem sys) {
    sys_ = sys;
    I_FabricateConstants constants = sys.getConstants();
    clConstants_ = constants.getCommandLineConstants();
    
    log_ = sys.getLog();
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
    setup();
  }
  
  public String getFabricateXmlPath() {
    if (fabricateXml_ == null) {
      return null;
    }
    return fabricateXml_.getAbsolutePath();
  }


  public String getProjectXml() {
    if (projectXml_ == null) {
      return null;
    }
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
  
  
  private void setup() {
    
    if (files_.exists("fabricate.xml")) {
      fabricateXml_ = files_.instance("fabricate.xml");
    } else {
      if (files_.exists("project.xml")) {
        projectXml_ = files_.instance("project.xml");
        //when a file instance is created with out a absolute path, the getParentFile methods return null;
        String absPath = projectXml_.getAbsolutePath();
        projectXml_ = files_.instance(absPath);
        
        File projectDir = projectXml_.getParentFile();
        if (projectDir != null) {
          File projectsDir = projectDir.getParentFile();
          if (projectsDir != null) {
            String devXmlFilePath = projectsDir.getAbsolutePath() + files_.getNameSeparator() + "dev.xml";
            
            File projectsParentDir = projectsDir.getParentFile();
            if (projectsParentDir != null) {
              if (files_.exists(devXmlFilePath)) {
                devXml_ = files_.instance(devXmlFilePath);
                try {
                  FabricateDevType dev = xmlFiles_.parseDev_v1_0(devXmlFilePath);
                  String projectGroup = dev.getProjectGroup();
                  String fabPath = projectsDir.getAbsolutePath() + files_.getNameSeparator() + 
                      projectGroup + files_.getNameSeparator() + "fabricate.xml";
                  fabricateXml_ = files_.instance(fabPath);
                 } catch (IOException e) {
                   devParseException_ = true;
                   if (log_.isLogEnabled(FabricateXmlDiscovery.class)) {
                     log_.printTrace(e);
                   }
                 } 
              } else {
                String fabPath = projectsParentDir.getAbsolutePath() + files_.getNameSeparator() + "fabricate.xml";
                fabricateXml_ = files_.instance(fabPath);
              }
            }
          }
        }
      }
    }
  }

  public boolean isDevParseException() {
    return devParseException_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_FabricateXmlDiscovery#getFabricateXmlDir()
   */
  @Override
  public String getFabricateXmlDir() {
    String absPath = fabricateXml_.getAbsolutePath();
    return FabFileUtils.getAbsoluteDir(absPath, files_.getNameSeparator().charAt(0));
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_FabricateXmlDiscovery#getProjectXmlDir()
   */
  @Override
  public String getProjectXmlDir() {
    if (projectXml_ == null) {
      return "";
    }
    String absPath = projectXml_.getAbsolutePath();
    return FabFileUtils.getAbsoluteDir(absPath, files_.getNameSeparator().charAt(0));
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.system.I_FabricateXmlDiscovery#getDevXmlDir()
   */
  @Override
  public String getDevXmlDir() {
    if (devXml_ == null) {
      return "";
    }
    String absPath = devXml_.getAbsolutePath();
    return FabFileUtils.getAbsoluteDir(absPath, files_.getNameSeparator().charAt(0));
  }

  @Override
  public String getProjectsDir() {
    if (devXml_ != null) {
      String dir = devXml_.getAbsolutePath();
      return files_.getParentDir(dir);
    }
    if (projectXml_ != null) {
      String proj = projectXml_.getAbsolutePath();
      return files_.getParentDir(files_.getParentDir(proj));
    }
    String devMode = clConstants_.getDevelopment(true);
    String fabXmlDir = getFabricateXmlDir();
    if (sys_.hasArg(devMode)) {
      return files_.getParentDir(fabXmlDir);
    } else {
      return fabXmlDir + "projects" + files_.getNameSeparator();
    }
  }
}
