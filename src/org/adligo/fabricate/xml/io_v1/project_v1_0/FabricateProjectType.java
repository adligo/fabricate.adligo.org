//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 02:17:56 AM CST 
//


package org.adligo.fabricate.xml.io_v1.project_v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependenciesType;


/**
 * <p>Java class for fabricate_project_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fabricate_project_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="command" type="{http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd}project_command_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="stages" type="{http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd}project_stages_type"/&gt;
 *         &lt;element name="dependencies" type="{http://www.adligo.org/fabricate/xml/io_v1/library_v1_0.xsd}dependencies_type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fabricate_project_type", propOrder = {
    "command",
    "stages",
    "dependencies"
})
public class FabricateProjectType {

    protected List<ProjectCommandType> command;
    @XmlElement(required = true)
    protected ProjectStagesType stages;
    protected DependenciesType dependencies;

    /**
     * Gets the value of the command property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the command property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommand().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectCommandType }
     * 
     * 
     */
    public List<ProjectCommandType> getCommand() {
        if (command == null) {
            command = new ArrayList<ProjectCommandType>();
        }
        return this.command;
    }

    /**
     * Gets the value of the stages property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectStagesType }
     *     
     */
    public ProjectStagesType getStages() {
        return stages;
    }

    /**
     * Sets the value of the stages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectStagesType }
     *     
     */
    public void setStages(ProjectStagesType value) {
        this.stages = value;
    }

    /**
     * Gets the value of the dependencies property.
     * 
     * @return
     *     possible object is
     *     {@link DependenciesType }
     *     
     */
    public DependenciesType getDependencies() {
        return dependencies;
    }

    /**
     * Sets the value of the dependencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link DependenciesType }
     *     
     */
    public void setDependencies(DependenciesType value) {
        this.dependencies = value;
    }

}
