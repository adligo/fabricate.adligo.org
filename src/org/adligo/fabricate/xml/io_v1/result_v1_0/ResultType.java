//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.31 at 05:31:18 PM CDT 
//


package org.adligo.fabricate.xml.io_v1.result_v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


/**
 * <p>Java class for result_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="result_type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="machine" type="{http://www.adligo.org/fabricate/xml/io_v1/result_v1_0.xsd}machine_info_type"/&gt;
 *         &lt;element name="tests" type="{http://www.adligo.org/fabricate/xml/io_v1/result_v1_0.xsd}tests_type" minOccurs="0"/&gt;
 *         &lt;element name="failure" type="{http://www.adligo.org/fabricate/xml/io_v1/result_v1_0.xsd}failure_type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}duration" /&gt;
 *       &lt;attribute name="os" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="os_version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="command_line" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "result_type", propOrder = {
    "machine",
    "tests",
    "failure"
})
public class ResultType {

    @XmlElement(required = true)
    protected MachineInfoType machine;
    protected TestsType tests;
    protected FailureType failure;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "successful")
    protected Boolean successful;
    @XmlAttribute(name = "duration")
    protected Duration duration;
    @XmlAttribute(name = "os")
    protected String os;
    @XmlAttribute(name = "os_version")
    protected String osVersion;
    @XmlAttribute(name = "command_line")
    protected String commandLine;

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
     * Gets the value of the tests property.
     * 
     * @return
     *     possible object is
     *     {@link TestsType }
     *     
     */
    public TestsType getTests() {
        return tests;
    }

    /**
     * Sets the value of the tests property.
     * 
     * @param value
     *     allowed object is
     *     {@link TestsType }
     *     
     */
    public void setTests(TestsType value) {
        this.tests = value;
    }

    /**
     * Gets the value of the failure property.
     * 
     * @return
     *     possible object is
     *     {@link FailureType }
     *     
     */
    public FailureType getFailure() {
        return failure;
    }

    /**
     * Sets the value of the failure property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailureType }
     *     
     */
    public void setFailure(FailureType value) {
        this.failure = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the successful property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSuccessful() {
        return successful;
    }

    /**
     * Sets the value of the successful property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSuccessful(Boolean value) {
        this.successful = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDuration(Duration value) {
        this.duration = value;
    }

    /**
     * Gets the value of the os property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOs() {
        return os;
    }

    /**
     * Sets the value of the os property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOs(String value) {
        this.os = value;
    }

    /**
     * Gets the value of the osVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * Sets the value of the osVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOsVersion(String value) {
        this.osVersion = value;
    }

    /**
     * Gets the value of the commandLine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommandLine() {
        return commandLine;
    }

    /**
     * Sets the value of the commandLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommandLine(String value) {
        this.commandLine = value;
    }

}
