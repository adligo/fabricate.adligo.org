package org.adligo.fabricate.common;

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
  
  public FabricateXmlDiscovery(I_FabLog log) {
    fabricateXml = new File("fabricate.xml");
    if (!fabricateXml.exists()) {
      projectXml = new File("project.xml");
      File dir = projectXml.getParentFile();
      if (dir != null) {
         File dirParent = dir.getParentFile();
         File devXml = new File(dirParent.getAbsolutePath() + File.pathSeparator + "dev.xml");
         if (!devXml.exists()) {
           return;
         } else {
         
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

  public File getFabricateXml() {
    return fabricateXml;
  }

  public File getProjectXml() {
    return projectXml;
  }
}
