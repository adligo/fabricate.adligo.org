package org.adligo.fabricate.files.xml_io;

import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

public class LibraryIO {
  
  @SuppressWarnings("unchecked")
  protected static LibraryType parse_v1_0(Schema schema,  File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(SchemaLoader.JAVA_LIB_NS_V1_0);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(schema);
      JAXBElement<LibraryType> devType = (JAXBElement<LibraryType>) jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
}
