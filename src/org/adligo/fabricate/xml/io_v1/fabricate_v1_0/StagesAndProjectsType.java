//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.25 at 07:04:57 PM CDT 
//


package org.adligo.fabricate.xml.io_v1.fabricate_v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stages_and_projects_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stages_and_projects_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="stages" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}stages_type" minOccurs="0"/&gt;
 *         &lt;element name="projects" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}projects_type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stages_and_projects_type", propOrder = {
    "stages",
    "projects"
})
public class StagesAndProjectsType {

    protected StagesType stages;
    @XmlElement(required = true)
    protected ProjectsType projects;

    /**
     * Gets the value of the stages property.
     * 
     * @return
     *     possible object is
     *     {@link StagesType }
     *     
     */
    public StagesType getStages() {
        return stages;
    }

    /**
     * Sets the value of the stages property.
     * 
     * @param value
     *     allowed object is
     *     {@link StagesType }
     *     
     */
    public void setStages(StagesType value) {
        this.stages = value;
    }

    /**
     * Gets the value of the projects property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectsType }
     *     
     */
    public ProjectsType getProjects() {
        return projects;
    }

    /**
     * Sets the value of the projects property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectsType }
     *     
     */
    public void setProjects(ProjectsType value) {
        this.projects = value;
    }

}
