package org.adligo.fabricate.common.files.xml_io;

import org.adligo.fabricate.xml.io_v1.result_v1_0.ResultType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;

public class ResultIO {

  @SuppressWarnings("unchecked")
  protected static ResultType parse_v1_0(Schema schema, File file) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(SchemaLoader.JAVA_PROJECT_NS_V1_0);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      
      jaxbUnmarshaller.setSchema(schema);
      JAXBElement<ResultType> devType = (JAXBElement<ResultType>) jaxbUnmarshaller.unmarshal(file);
      ResultType toRet = devType.getValue();
      return toRet;
    } catch (JAXBException e) {
      throw new IOException(file.getAbsolutePath(), e);
    } 
  }
  
  protected static void write_v1_0(String filePath, ResultType result) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(SchemaLoader.JAVA_RESULT_NS_V1_0);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      
      marshaller.marshal(new JAXBElement<ResultType>(
            new QName(SchemaLoader.JAVA_RESULT_NS_V1_0,
            "result"), ResultType.class, result), new File(filePath));
    } catch (JAXBException e) {
      throw new IOException(e);
    }
  }
}
