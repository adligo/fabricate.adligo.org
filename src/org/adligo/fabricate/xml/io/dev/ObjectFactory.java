//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.24 at 02:33:10 PM CDT 
//


package org.adligo.fabricate.xml.io.dev;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.adligo.fabricate.xml.io.dev package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Dev_QNAME = new QName("http://www.adligo.org/fabricate/xml/io/dev", "dev");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.adligo.fabricate.xml.io.dev
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FabricateDevType }
     * 
     */
    public FabricateDevType createFabricateDevType() {
        return new FabricateDevType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FabricateDevType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.adligo.org/fabricate/xml/io/dev", name = "dev")
    public JAXBElement<FabricateDevType> createDev(FabricateDevType value) {
        return new JAXBElement<FabricateDevType>(_Dev_QNAME, FabricateDevType.class, null, value);
    }

}
