//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.27 at 02:12:24 PM CDT 
//


package org.adligo.fabricate.xml.io;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *     			This provides a limited list of options to pass to the
 *     			jvm which actually does the fabrication. These options
 *     			are peers of options listed here
 *     			http://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html
 *     		
 * 
 * <p>Java class for java_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="java_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="Xmx" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="Xms" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="threads" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "java_type")
public class JavaType {

    @XmlAttribute(name = "Xmx")
    protected String xmx;
    @XmlAttribute(name = "Xms")
    protected String xms;
    @XmlAttribute(name = "threads")
    protected Integer threads;

    /**
     * Gets the value of the xmx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmx() {
        return xmx;
    }

    /**
     * Sets the value of the xmx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmx(String value) {
        this.xmx = value;
    }

    /**
     * Gets the value of the xms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXms() {
        return xms;
    }

    /**
     * Sets the value of the xms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXms(String value) {
        this.xms = value;
    }

    /**
     * Gets the value of the threads property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThreads() {
        return threads;
    }

    /**
     * Sets the value of the threads property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThreads(Integer value) {
        this.threads = value;
    }

}
