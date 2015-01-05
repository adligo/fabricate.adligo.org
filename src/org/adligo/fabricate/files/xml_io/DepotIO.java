package org.adligo.fabricate.files.xml_io;

import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;

public class DepotIO {
  
  protected static void write_v1_0(File file, DepotType depot) throws IOException {
    try {
      Marshaller marshaller = FabricateJaxbContexts.DEPOT_CONTEXT.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(new JAXBElement<DepotType>(
          new QName(SchemaLoader.DEPOT_NS_V1_0,
          "depot"), DepotType.class, depot), file);
    } catch (Exception e) {
      throw new IOException(e);
    } 
  }
  
  @SuppressWarnings("unchecked")
  protected static DepotType parse_v1_0(Schema schema, File file) throws IOException {
    try {
      Unmarshaller jaxbUnmarshaller = FabricateJaxbContexts.DEPOT_CONTEXT.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(schema);
      JAXBElement<DepotType> devType = (JAXBElement<DepotType>) jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
}
