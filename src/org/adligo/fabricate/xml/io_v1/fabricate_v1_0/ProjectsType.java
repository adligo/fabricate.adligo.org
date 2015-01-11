//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 02:17:56 AM CST 
//


package org.adligo.fabricate.xml.io_v1.fabricate_v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for projects_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="projects_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="scm" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}scm_type"/&gt;
 *         &lt;element name="project" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}project_type" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "projects_type", propOrder = {
    "scm",
    "project"
})
public class ProjectsType {

    @XmlElement(required = true)
    protected ScmType scm;
    @XmlElement(required = true)
    protected List<ProjectType> project;

    /**
     * Gets the value of the scm property.
     * 
     * @return
     *     possible object is
     *     {@link ScmType }
     *     
     */
    public ScmType getScm() {
        return scm;
    }

    /**
     * Sets the value of the scm property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScmType }
     *     
     */
    public void setScm(ScmType value) {
        this.scm = value;
    }

    /**
     * Gets the value of the project property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the project property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectType }
     * 
     * 
     */
    public List<ProjectType> getProject() {
        if (project == null) {
            project = new ArrayList<ProjectType>();
        }
        return this.project;
    }

}
