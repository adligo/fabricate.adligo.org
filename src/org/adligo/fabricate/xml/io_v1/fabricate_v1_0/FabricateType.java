//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.25 at 07:04:57 PM CDT 
//


package org.adligo.fabricate.xml.io_v1.fabricate_v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;


/**
 * 
 * 				Fabricate type represents a complete fabrication environment, which allows the user to execute commands or stages.  
 * 				Commands allow the environment to be customized for a particular purpose (like Eclipse development).  Stages are 
 * 				executed on the environment as a ordered group (a process refereed to as fabrication).
 * 				   Commands, Facets, Stages, Tasks and their tasks must be implementations of org.adligo.fabricate.common.I_FabricationRoutine. 
 * 				Commands and Stages may execute in a one off, single threaded, concurrent, project dependent, or customizable manor 
 * 				based on additional interfaces they implement.  Traits allow plug-able code to be shared between Commands and Stages.  
 * 				Projects may override trait classes but may NOT override command or stage classes.  Projects may always override parameters, 
 * 				and participate in stages or commands and their tasks.  Facets are used to obtain and load projects and their dependencies.   
 * 				   The elements of the fabricate_type are ordered by importance, and use by the fabricate program.  
 * 				   
 * 				  
 * 			
 * 
 * <p>Java class for fabricate_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fabricate_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="java" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}java_type" minOccurs="0"/&gt;
 *         &lt;element name="logs" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}log_settings_type" minOccurs="0"/&gt;
 *         &lt;element name="attributes" type="{http://www.adligo.org/fabricate/xml/io_v1/common_v1_0.xsd}params_type" minOccurs="0"/&gt;
 *         &lt;element name="dependencies" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}fabricate_dependencies"/&gt;
 *         &lt;element name="trait" type="{http://www.adligo.org/fabricate/xml/io_v1/common_v1_0.xsd}routine_parent_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="facet" type="{http://www.adligo.org/fabricate/xml/io_v1/common_v1_0.xsd}routine_parent_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="command" type="{http://www.adligo.org/fabricate/xml/io_v1/common_v1_0.xsd}routine_parent_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="groups" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}project_groups_type" minOccurs="0"/&gt;
 *           &lt;element name="project_group" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}stages_and_projects_type" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fabricate_type", propOrder = {
    "java",
    "logs",
    "attributes",
    "dependencies",
    "trait",
    "facet",
    "command",
    "groups",
    "projectGroup"
})
public class FabricateType {

    protected JavaType java;
    protected LogSettingsType logs;
    protected ParamsType attributes;
    @XmlElement(required = true)
    protected FabricateDependencies dependencies;
    protected List<RoutineParentType> trait;
    protected List<RoutineParentType> facet;
    protected List<RoutineParentType> command;
    protected ProjectGroupsType groups;
    @XmlElement(name = "project_group")
    protected StagesAndProjectsType projectGroup;

    /**
     * Gets the value of the java property.
     * 
     * @return
     *     possible object is
     *     {@link JavaType }
     *     
     */
    public JavaType getJava() {
        return java;
    }

    /**
     * Sets the value of the java property.
     * 
     * @param value
     *     allowed object is
     *     {@link JavaType }
     *     
     */
    public void setJava(JavaType value) {
        this.java = value;
    }

    /**
     * Gets the value of the logs property.
     * 
     * @return
     *     possible object is
     *     {@link LogSettingsType }
     *     
     */
    public LogSettingsType getLogs() {
        return logs;
    }

    /**
     * Sets the value of the logs property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogSettingsType }
     *     
     */
    public void setLogs(LogSettingsType value) {
        this.logs = value;
    }

    /**
     * Gets the value of the attributes property.
     * 
     * @return
     *     possible object is
     *     {@link ParamsType }
     *     
     */
    public ParamsType getAttributes() {
        return attributes;
    }

    /**
     * Sets the value of the attributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamsType }
     *     
     */
    public void setAttributes(ParamsType value) {
        this.attributes = value;
    }

    /**
     * Gets the value of the dependencies property.
     * 
     * @return
     *     possible object is
     *     {@link FabricateDependencies }
     *     
     */
    public FabricateDependencies getDependencies() {
        return dependencies;
    }

    /**
     * Sets the value of the dependencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link FabricateDependencies }
     *     
     */
    public void setDependencies(FabricateDependencies value) {
        this.dependencies = value;
    }

    /**
     * Gets the value of the trait property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trait property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrait().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutineParentType }
     * 
     * 
     */
    public List<RoutineParentType> getTrait() {
        if (trait == null) {
            trait = new ArrayList<RoutineParentType>();
        }
        return this.trait;
    }

    /**
     * Gets the value of the facet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutineParentType }
     * 
     * 
     */
    public List<RoutineParentType> getFacet() {
        if (facet == null) {
            facet = new ArrayList<RoutineParentType>();
        }
        return this.facet;
    }

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
     * {@link RoutineParentType }
     * 
     * 
     */
    public List<RoutineParentType> getCommand() {
        if (command == null) {
            command = new ArrayList<RoutineParentType>();
        }
        return this.command;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectGroupsType }
     *     
     */
    public ProjectGroupsType getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectGroupsType }
     *     
     */
    public void setGroups(ProjectGroupsType value) {
        this.groups = value;
    }

    /**
     * Gets the value of the projectGroup property.
     * 
     * @return
     *     possible object is
     *     {@link StagesAndProjectsType }
     *     
     */
    public StagesAndProjectsType getProjectGroup() {
        return projectGroup;
    }

    /**
     * Sets the value of the projectGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link StagesAndProjectsType }
     *     
     */
    public void setProjectGroup(StagesAndProjectsType value) {
        this.projectGroup = value;
    }

}
