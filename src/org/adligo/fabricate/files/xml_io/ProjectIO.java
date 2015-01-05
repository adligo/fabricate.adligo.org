package org.adligo.fabricate.files.xml_io;

import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

/**
 * This class parses the project.xml files,
 * and keeps a cache of them as they generally 
 * don't change during a run.
 *
 * @author scott
 *
 */
public class ProjectIO {
  
  public static final String THERE_WAS_AN_ERROR_PARSING_THE_FOLLOWING_FILE = "There was an error parsing the following file;";

  @SuppressWarnings("unchecked")
  protected static FabricateProjectType parse_v1_0(Schema schema, File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(SchemaLoader.JAVA_PROJECT_NS_V1_0);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(schema);
      JAXBElement<FabricateProjectType> devType = (JAXBElement<FabricateProjectType>) jaxbUnmarshaller.unmarshal(file);
      FabricateProjectType toRet = devType.getValue();
      return toRet;
    } catch (JAXBException e) {
      throw new IOException(THERE_WAS_AN_ERROR_PARSING_THE_FOLLOWING_FILE +
          file.getAbsolutePath(), e);
    } 
  }
  
}
