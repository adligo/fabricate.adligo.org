//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.18 at 06:32:31 PM CST 
//


package org.adligo.fabricate.xml.io_v1.project_v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependenciesType;


/**
 * <p>Java class for project_dependencies_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="project_dependencies_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.adligo.org/fabricate/xml/io_v1/library_v1_0.xsd}dependencies_type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="project" type="{http://www.adligo.org/fabricate/xml/io_v1/project_v1_0.xsd}project_dependency_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project_dependencies_type", propOrder = {
    "project"
})
public class ProjectDependenciesType
    extends DependenciesType
{

    protected List<ProjectDependencyType> project;

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
     * {@link ProjectDependencyType }
     * 
     * 
     */
    public List<ProjectDependencyType> getProject() {
        if (project == null) {
            project = new ArrayList<ProjectDependencyType>();
        }
        return this.project;
    }

}
