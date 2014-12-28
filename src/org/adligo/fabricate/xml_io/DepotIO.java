package org.adligo.fabricate.xml_io;

import org.adligo.fabricate.xml.io.depot.v1_0.DepotType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

public class DepotIO {
  
  public static void write(File file, DepotType depot) throws IOException {
    try {
     
      
      Marshaller marshaller = FabricateJaxbContexts.DEPOT_CONTEXT.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(new JAXBElement<DepotType>(
          new QName("http://www.adligo.org/fabricate/xml/io/depot/v1_0",
          "depot"), DepotType.class, depot), file);
    } catch (Exception e) {
      throw new IOException(e);
    } 
  }
  @SuppressWarnings("unchecked")
  public static DepotType parse(File file) throws IOException {
    try {
      Unmarshaller jaxbUnmarshaller = FabricateJaxbContexts.DEPOT_CONTEXT.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(SchemaLoader.INSTANCE.get());
      JAXBElement<DepotType> devType = (JAXBElement<DepotType>) jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
}
