package org.adligo.fabricate.xml_io;

import org.adligo.fabricate.xml.io.project.FabricateProjectType;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * This class parses the project.xml files,
 * and keeps a cache of them as they generally 
 * don't change during a run.
 *
 * @author scott
 *
 */
public class ProjectIO {
  
  @SuppressWarnings("unchecked")
  public static FabricateProjectType parse(File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("org.adligo.fabricate.xml.io.project");
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(SchemaLoader.INSTANCE.get());
      JAXBElement<FabricateProjectType> devType = (JAXBElement<FabricateProjectType>) jaxbUnmarshaller.unmarshal(file);
      FabricateProjectType toRet = devType.getValue();
      return toRet;
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
  
}
