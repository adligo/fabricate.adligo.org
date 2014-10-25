package org.adligo.fabricate.parsers;

import org.adligo.fabricate.common.I_FabLog;
import org.adligo.fabricate.xml.io.FabricateType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class FabricateParser {
  private I_FabLog log_;
  
  public FabricateParser(I_FabLog log) throws IOException {
    log_ = log;
  }
  
  public FabricateType parse(File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("org.adligo.fabricate.xml.io");
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(SchemaLoader.INSTANCE.get());
      JAXBElement<FabricateType> devType = (JAXBElement<FabricateType>) jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      e.printStackTrace();
    } 
    return null;
  }
}
