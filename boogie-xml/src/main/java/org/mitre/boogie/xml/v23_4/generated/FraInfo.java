
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element provides boolean information about free route airspace
 * 
 * <p>Java class for FraInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FraInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isFraArrivalTransitionPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFraDepartureTransitionPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFraIntermediatePoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFraTerminalHoldingPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFraEntryPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFraExitPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FraInfo", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isFraArrivalTransitionPoint",
    "isFraDepartureTransitionPoint",
    "isFraIntermediatePoint",
    "isFraTerminalHoldingPoint",
    "isFraEntryPoint",
    "isFraExitPoint"
})
public class FraInfo {

    @XmlElement(defaultValue = "false")
    protected Boolean isFraArrivalTransitionPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFraDepartureTransitionPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFraIntermediatePoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFraTerminalHoldingPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFraEntryPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isFraExitPoint;

    /**
     * Gets the value of the isFraArrivalTransitionPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFraArrivalTransitionPoint() {
        return isFraArrivalTransitionPoint;
    }

    /**
     * Sets the value of the isFraArrivalTransitionPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFraArrivalTransitionPoint(Boolean value) {
        this.isFraArrivalTransitionPoint = value;
    }

    /**
     * Gets the value of the isFraDepartureTransitionPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFraDepartureTransitionPoint() {
        return isFraDepartureTransitionPoint;
    }

    /**
     * Sets the value of the isFraDepartureTransitionPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFraDepartureTransitionPoint(Boolean value) {
        this.isFraDepartureTransitionPoint = value;
    }

    /**
     * Gets the value of the isFraIntermediatePoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFraIntermediatePoint() {
        return isFraIntermediatePoint;
    }

    /**
     * Sets the value of the isFraIntermediatePoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFraIntermediatePoint(Boolean value) {
        this.isFraIntermediatePoint = value;
    }

    /**
     * Gets the value of the isFraTerminalHoldingPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFraTerminalHoldingPoint() {
        return isFraTerminalHoldingPoint;
    }

    /**
     * Sets the value of the isFraTerminalHoldingPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFraTerminalHoldingPoint(Boolean value) {
        this.isFraTerminalHoldingPoint = value;
    }

    /**
     * Gets the value of the isFraEntryPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFraEntryPoint() {
        return isFraEntryPoint;
    }

    /**
     * Sets the value of the isFraEntryPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFraEntryPoint(Boolean value) {
        this.isFraEntryPoint = value;
    }

    /**
     * Gets the value of the isFraExitPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFraExitPoint() {
        return isFraExitPoint;
    }

    /**
     * Sets the value of the isFraExitPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFraExitPoint(Boolean value) {
        this.isFraExitPoint = value;
    }

}
