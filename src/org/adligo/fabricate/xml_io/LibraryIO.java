package org.adligo.fabricate.xml_io;

import org.adligo.fabricate.xml.io.library.LibraryType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class LibraryIO {
  
  @SuppressWarnings("unchecked")
  public static LibraryType parse(File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("org.adligo.fabricate.xml.io.library");
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(SchemaLoader.INSTANCE.get());
      JAXBElement<LibraryType> devType = (JAXBElement<LibraryType>) jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
}
