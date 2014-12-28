package org.adligo.fabricate.xml_io;

import org.adligo.fabricate.xml.io.result.v1_0.ResultType;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

public class ResultIO {

  public static void write(ResultType result, String filePath) throws IOException {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("org.adligo.fabricate.xml.io.result.v1_0");
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      
      marshaller.marshal(new JAXBElement<ResultType>(
            new QName("http://www.adligo.org/fabricate/xml/io/result/v1_0",
            "result"), ResultType.class, result), new File(filePath));
    } catch (JAXBException e) {
      throw new IOException(e);
    }
  }
}
