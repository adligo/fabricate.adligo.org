//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.10 at 11:14:41 PM CST 
//


package org.adligo.fabricate.xml.io_v1.fabricate_v1_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.adligo.fabricate.xml.io_v1.fabricate_v1_0 package. 
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

    private final static QName _Fabricate_QNAME = new QName("http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd", "fabricate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.adligo.fabricate.xml.io_v1.fabricate_v1_0
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FabricateType }
     * 
     */
    public FabricateType createFabricateType() {
        return new FabricateType();
    }

    /**
     * Create an instance of {@link ProjectGroupsType }
     * 
     */
    public ProjectGroupsType createProjectGroupsType() {
        return new ProjectGroupsType();
    }

    /**
     * Create an instance of {@link GitServerType }
     * 
     */
    public GitServerType createGitServerType() {
        return new GitServerType();
    }

    /**
     * Create an instance of {@link ProjectsType }
     * 
     */
    public ProjectsType createProjectsType() {
        return new ProjectsType();
    }

    /**
     * Create an instance of {@link ScmType }
     * 
     */
    public ScmType createScmType() {
        return new ScmType();
    }

    /**
     * Create an instance of {@link ProjectType }
     * 
     */
    public ProjectType createProjectType() {
        return new ProjectType();
    }

    /**
     * Create an instance of {@link FabricateDependencies }
     * 
     */
    public FabricateDependencies createFabricateDependencies() {
        return new FabricateDependencies();
    }

    /**
     * Create an instance of {@link StageType }
     * 
     */
    public StageType createStageType() {
        return new StageType();
    }

    /**
     * Create an instance of {@link StagesType }
     * 
     */
    public StagesType createStagesType() {
        return new StagesType();
    }

    /**
     * Create an instance of {@link StagesAndProjectsType }
     * 
     */
    public StagesAndProjectsType createStagesAndProjectsType() {
        return new StagesAndProjectsType();
    }

    /**
     * Create an instance of {@link JavaType }
     * 
     */
    public JavaType createJavaType() {
        return new JavaType();
    }

    /**
     * Create an instance of {@link LogSettingType }
     * 
     */
    public LogSettingType createLogSettingType() {
        return new LogSettingType();
    }

    /**
     * Create an instance of {@link LogSettingsType }
     * 
     */
    public LogSettingsType createLogSettingsType() {
        return new LogSettingsType();
    }

    /**
     * Create an instance of {@link CommandType }
     * 
     */
    public CommandType createCommandType() {
        return new CommandType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FabricateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd", name = "fabricate")
    public JAXBElement<FabricateType> createFabricate(FabricateType value) {
        return new JAXBElement<FabricateType>(_Fabricate_QNAME, FabricateType.class, null, value);
    }

}
