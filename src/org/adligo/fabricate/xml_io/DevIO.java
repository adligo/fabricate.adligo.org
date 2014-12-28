package org.adligo.fabricate.xml_io;

import org.adligo.fabricate.xml.io.dev.v1_0.FabricateDevType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

public class DevIO {
  
  public static final String NAMESPACE_NAME = "org.adligo.fabricate.xml.io.dev.v1_0";
  public static final QName NAMESPACE_QNAME = new QName("http://www.adligo.org/fabricate/xml/io/dev/v1_0","dev");
  
  @SuppressWarnings("unchecked")
  public static FabricateDevType parse(File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(NAMESPACE_NAME);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(SchemaLoader.INSTANCE.get());
      JAXBElement<FabricateDevType> devType = (JAXBElement<FabricateDevType>) 
            jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
  
  public static void write(FabricateDevType result, String filePath) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(NAMESPACE_NAME);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      
      marshaller.marshal(new JAXBElement<FabricateDevType>(
          NAMESPACE_QNAME, FabricateDevType.class, result), new File(filePath));
    } catch (JAXBException e) {
      throw new IOException(e);
    }
  }
}
