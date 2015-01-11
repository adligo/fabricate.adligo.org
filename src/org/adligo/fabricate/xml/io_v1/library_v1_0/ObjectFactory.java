//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 02:17:56 AM CST 
//


package org.adligo.fabricate.xml.io_v1.library_v1_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.adligo.fabricate.xml.io_v1.library_v1_0 package. 
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

    private final static QName _Library_QNAME = new QName("http://www.adligo.org/fabricate/xml/io_v1/library_v1_0.xsd", "library");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.adligo.fabricate.xml.io_v1.library_v1_0
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LibraryType }
     * 
     */
    public LibraryType createLibraryType() {
        return new LibraryType();
    }

    /**
     * Create an instance of {@link ProjectDependencyType }
     * 
     */
    public ProjectDependencyType createProjectDependencyType() {
        return new ProjectDependencyType();
    }

    /**
     * Create an instance of {@link DependencyType }
     * 
     */
    public DependencyType createDependencyType() {
        return new DependencyType();
    }

    /**
     * Create an instance of {@link DependenciesType }
     * 
     */
    public DependenciesType createDependenciesType() {
        return new DependenciesType();
    }

    /**
     * Create an instance of {@link LibraryReferenceType }
     * 
     */
    public LibraryReferenceType createLibraryReferenceType() {
        return new LibraryReferenceType();
    }

    /**
     * Create an instance of {@link IdeArgumentType }
     * 
     */
    public IdeArgumentType createIdeArgumentType() {
        return new IdeArgumentType();
    }

    /**
     * Create an instance of {@link IdeType }
     * 
     */
    public IdeType createIdeType() {
        return new IdeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LibraryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.adligo.org/fabricate/xml/io_v1/library_v1_0.xsd", name = "library")
    public JAXBElement<LibraryType> createLibrary(LibraryType value) {
        return new JAXBElement<LibraryType>(_Library_QNAME, LibraryType.class, null, value);
    }

}
