//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.10 at 08:14:16 PM CST 
//


package org.adligo.fabricate.xml.io_v1.fabricate_v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scm_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scm_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="git" type="{http://www.adligo.org/fabricate/xml/io_v1/fabricate_v1_0.xsd}git_server_type"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scm_type", propOrder = {
    "git"
})
public class ScmType {

    protected GitServerType git;

    /**
     * Gets the value of the git property.
     * 
     * @return
     *     possible object is
     *     {@link GitServerType }
     *     
     */
    public GitServerType getGit() {
        return git;
    }

    /**
     * Sets the value of the git property.
     * 
     * @param value
     *     allowed object is
     *     {@link GitServerType }
     *     
     */
    public void setGit(GitServerType value) {
        this.git = value;
    }

}
