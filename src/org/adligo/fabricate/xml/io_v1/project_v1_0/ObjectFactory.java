//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.23 at 02:35:30 PM CST 
//


package org.adligo.fabricate.xml.io_v1.project_v1_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.adligo.fabricate.xml.io_v1.project_v1_0 package. 
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

    private final static QName _FabricateProject_QNAME = new QName("http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd", "fabricate_project");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.adligo.fabricate.xml.io_v1.project_v1_0
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FabricateProjectType }
     * 
     */
    public FabricateProjectType createFabricateProjectType() {
        return new FabricateProjectType();
    }

    /**
     * Create an instance of {@link ProjectStagesType }
     * 
     */
    public ProjectStagesType createProjectStagesType() {
        return new ProjectStagesType();
    }

    /**
     * Create an instance of {@link ProjectDependencyType }
     * 
     */
    public ProjectDependencyType createProjectDependencyType() {
        return new ProjectDependencyType();
    }

    /**
     * Create an instance of {@link ProjectDependenciesType }
     * 
     */
    public ProjectDependenciesType createProjectDependenciesType() {
        return new ProjectDependenciesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FabricateProjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd", name = "fabricate_project")
    public JAXBElement<FabricateProjectType> createFabricateProject(FabricateProjectType value) {
        return new JAXBElement<FabricateProjectType>(_FabricateProject_QNAME, FabricateProjectType.class, null, value);
    }

}
