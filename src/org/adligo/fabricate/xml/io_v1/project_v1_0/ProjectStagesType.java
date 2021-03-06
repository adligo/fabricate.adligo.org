//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.31 at 05:31:18 PM CDT 
//


package org.adligo.fabricate.xml.io_v1.project_v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * 
 * <p>Java class for project_stages_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="project_stages_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="stage" type="{http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd}project_routine_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="projectArchiveStage" type="{http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd}project_routine_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project_stages_type", propOrder = {
    "stage",
    "projectArchiveStage"
})
public class ProjectStagesType {

    protected List<ProjectRoutineType> stage;
    protected List<ProjectRoutineType> projectArchiveStage;

    /**
     * Gets the value of the stage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectRoutineType }
     * 
     * 
     */
    public List<ProjectRoutineType> getStage() {
        if (stage == null) {
            stage = new ArrayList<ProjectRoutineType>();
        }
        return this.stage;
    }

    /**
     * Gets the value of the projectArchiveStage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the projectArchiveStage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProjectArchiveStage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectRoutineType }
     * 
     * 
     */
    public List<ProjectRoutineType> getProjectArchiveStage() {
        if (projectArchiveStage == null) {
            projectArchiveStage = new ArrayList<ProjectRoutineType>();
        }
        return this.projectArchiveStage;
    }

}
