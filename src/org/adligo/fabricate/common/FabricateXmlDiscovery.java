package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.dev.FabricateDevType;
import org.adligo.fabricate.xml_io.DevIO;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

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
  
  public FabricateXmlDiscovery() {
    fabricateXml = new File("fabricate.xml");
    if (!fabricateXml.exists()) {
      projectXml = new File("project.xml");
      File dir = projectXml.getParentFile();
      if (dir != null) {
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
            //do nothing
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
