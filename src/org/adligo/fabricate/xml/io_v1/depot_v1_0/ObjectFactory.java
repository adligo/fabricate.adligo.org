//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.14 at 05:44:55 PM CDT 
//


package org.adligo.fabricate.xml.io_v1.depot_v1_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.adligo.fabricate.xml.io_v1.depot_v1_0 package. 
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

    private final static QName _Depot_QNAME = new QName("http://www.adligo.org/fabricate/xml/io_v1/depot_v1_0.xsd", "depot");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.adligo.fabricate.xml.io_v1.depot_v1_0
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DepotType }
     * 
     */
    public DepotType createDepotType() {
        return new DepotType();
    }

    /**
     * Create an instance of {@link ArtifactType }
     * 
     */
    public ArtifactType createArtifactType() {
        return new ArtifactType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DepotType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.adligo.org/fabricate/xml/io_v1/depot_v1_0.xsd", name = "depot")
    public JAXBElement<DepotType> createDepot(DepotType value) {
        return new JAXBElement<DepotType>(_Depot_QNAME, DepotType.class, null, value);
    }

}
