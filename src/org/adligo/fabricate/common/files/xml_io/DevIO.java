package org.adligo.fabricate.common.files.xml_io;

import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;

public class DevIO {
  /**
   * public for testing only.
   * Use I_FabFiles(Files) for file Api stubbing.
   */
  public static final QName NAMESPACE_QNAME_V1_0 = new QName(SchemaLoader.DEV_NS_V1_0,"dev");
  
  @SuppressWarnings("unchecked")
  protected static FabricateDevType parse_v1_0(Schema schema, File file) throws IOException {
    try {
      Unmarshaller jaxbUnmarshaller = FabricateJaxbContexts.DEV_CONTEXT.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(schema);
      JAXBElement<FabricateDevType> devType = (JAXBElement<FabricateDevType>) 
            jaxbUnmarshaller.unmarshal(file);
      return devType.getValue();
    } catch (JAXBException e) {
      throw new IOException(e);
    } 
  }
  
  protected static void write_v1_0(String filePath, FabricateDevType result) throws IOException {
    try {
      Marshaller marshaller = FabricateJaxbContexts.DEV_CONTEXT.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      
      marshaller.marshal(new JAXBElement<FabricateDevType>(
          NAMESPACE_QNAME_V1_0, FabricateDevType.class, result), new File(filePath));
    } catch (JAXBException e) {
      throw new IOException(e);
    }
  }
}
