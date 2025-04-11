
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction used to represent the vertical details of Cruise Direction.
 * 
 * <p>Java class for CruiseVerticalDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CruiseVerticalDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cruiseLevelFrom" type="{http://www.arinc424.com/xml/datatypes}CruiseLevelOrSeparation"/&gt;
 *         &lt;element name="cruiseLevelTo" type="{http://www.arinc424.com/xml/datatypes}CruiseLevelOrSeparation"/&gt;
 *         &lt;element name="verticalSeparation" type="{http://www.arinc424.com/xml/datatypes}CruiseLevelOrSeparation"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CruiseVerticalDetails", propOrder = {
    "cruiseLevelFrom",
    "cruiseLevelTo",
    "verticalSeparation"
})
public class CruiseVerticalDetails {

    @XmlElement(required = true)
    protected CruiseLevelOrSeparation cruiseLevelFrom;
    @XmlElement(required = true)
    protected CruiseLevelOrSeparation cruiseLevelTo;
    @XmlElement(required = true)
    protected CruiseLevelOrSeparation verticalSeparation;

    /**
     * Gets the value of the cruiseLevelFrom property.
     * 
     * @return
     *     possible object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public CruiseLevelOrSeparation getCruiseLevelFrom() {
        return cruiseLevelFrom;
    }

    /**
     * Sets the value of the cruiseLevelFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public void setCruiseLevelFrom(CruiseLevelOrSeparation value) {
        this.cruiseLevelFrom = value;
    }

    /**
     * Gets the value of the cruiseLevelTo property.
     * 
     * @return
     *     possible object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public CruiseLevelOrSeparation getCruiseLevelTo() {
        return cruiseLevelTo;
    }

    /**
     * Sets the value of the cruiseLevelTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public void setCruiseLevelTo(CruiseLevelOrSeparation value) {
        this.cruiseLevelTo = value;
    }

    /**
     * Gets the value of the verticalSeparation property.
     * 
     * @return
     *     possible object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public CruiseLevelOrSeparation getVerticalSeparation() {
        return verticalSeparation;
    }

    /**
     * Sets the value of the verticalSeparation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CruiseLevelOrSeparation }
     *     
     */
    public void setVerticalSeparation(CruiseLevelOrSeparation value) {
        this.verticalSeparation = value;
    }

}
