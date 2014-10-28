//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.28 at 12:22:38 AM CDT 
//


package org.adligo.fabricate.xml.io.depot;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.adligo.fabricate.xml.io.depot package. 
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

    private final static QName _Depot_QNAME = new QName("http://www.adligo.org/fabricate/xml/io/depot", "depot");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.adligo.fabricate.xml.io.depot
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
     * Create an instance of {@link ProjectArtifactsType }
     * 
     */
    public ProjectArtifactsType createProjectArtifactsType() {
        return new ProjectArtifactsType();
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
    @XmlElementDecl(namespace = "http://www.adligo.org/fabricate/xml/io/depot", name = "depot")
    public JAXBElement<DepotType> createDepot(DepotType value) {
        return new JAXBElement<DepotType>(_Depot_QNAME, DepotType.class, null, value);
    }

}
