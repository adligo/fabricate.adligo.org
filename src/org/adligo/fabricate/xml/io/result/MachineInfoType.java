//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.24 at 02:33:10 PM CDT 
//


package org.adligo.fabricate.xml.io.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for machine_info_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="machine_info_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="machine" type="{http://www.adligo.org/fabricate/xml/io/result}machine_info_type"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="hostname" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="cpu_name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="cpu_speed" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ram" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="processors" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "machine_info_type", propOrder = {
    "machine"
})
public class MachineInfoType {

    @XmlElement(required = true)
    protected MachineInfoType machine;
    @XmlAttribute(name = "hostname")
    protected String hostname;
    @XmlAttribute(name = "cpu_name")
    protected String cpuName;
    @XmlAttribute(name = "cpu_speed")
    protected String cpuSpeed;
    @XmlAttribute(name = "ram")
    protected String ram;
    @XmlAttribute(name = "processors")
    protected String processors;

    /**
     * Gets the value of the machine property.
     * 
     * @return
     *     possible object is
     *     {@link MachineInfoType }
     *     
     */
    public MachineInfoType getMachine() {
        return machine;
    }

    /**
     * Sets the value of the machine property.
     * 
     * @param value
     *     allowed object is
     *     {@link MachineInfoType }
     *     
     */
    public void setMachine(MachineInfoType value) {
        this.machine = value;
    }

    /**
     * Gets the value of the hostname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the value of the hostname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostname(String value) {
        this.hostname = value;
    }

    /**
     * Gets the value of the cpuName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpuName() {
        return cpuName;
    }

    /**
     * Sets the value of the cpuName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpuName(String value) {
        this.cpuName = value;
    }

    /**
     * Gets the value of the cpuSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpuSpeed() {
        return cpuSpeed;
    }

    /**
     * Sets the value of the cpuSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpuSpeed(String value) {
        this.cpuSpeed = value;
    }

    /**
     * Gets the value of the ram property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRam() {
        return ram;
    }

    /**
     * Sets the value of the ram property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRam(String value) {
        this.ram = value;
    }

    /**
     * Gets the value of the processors property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessors() {
        return processors;
    }

    /**
     * Sets the value of the processors property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessors(String value) {
        this.processors = value;
    }

}
