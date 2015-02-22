//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.22 at 12:49:14 AM CST 
//


package org.adligo.fabricate.xml.io_v1.library_v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.ProjectDependenciesType;


/**
 * <p>Java class for dependencies_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dependencies_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="library" type="{http://www.adligo.org/fabricate/xml/io_v1/library_v1_0.xsd}library_reference_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dependency" type="{http://www.adligo.org/fabricate/xml/io_v1/library_v1_0.xsd}dependency_type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dependencies_type", propOrder = {
    "library",
    "dependency"
})
@XmlSeeAlso({
    ProjectDependenciesType.class
})
public class DependenciesType {

    protected List<LibraryReferenceType> library;
    protected List<DependencyType> dependency;

    /**
     * Gets the value of the library property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the library property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibrary().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryReferenceType }
     * 
     * 
     */
    public List<LibraryReferenceType> getLibrary() {
        if (library == null) {
            library = new ArrayList<LibraryReferenceType>();
        }
        return this.library;
    }

    /**
     * Gets the value of the dependency property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependency property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependency().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DependencyType }
     * 
     * 
     */
    public List<DependencyType> getDependency() {
        if (dependency == null) {
            dependency = new ArrayList<DependencyType>();
        }
        return this.dependency;
    }

}
