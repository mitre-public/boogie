
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element is the parent of altitudes on holds, airspaces, and routes.
 * 
 * <p>Java class for AirspaceRouteHoldAltitude complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirspaceRouteHoldAltitude"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue" minOccurs="0"/&gt;
 *         &lt;element name="isMsl" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNotam" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNotSpecified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFlightLevel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isUnknown" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirspaceRouteHoldAltitude", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "altitude",
    "isMsl",
    "isNotam",
    "isNotSpecified",
    "isFlightLevel",
    "isUnknown"
})
@XmlSeeAlso({
    UpperLimitConstraint.class,
    LowerLimitConstraint.class,
    RouteMinimumAltitude.class,
    RouteMaximumAltitude.class
})
public abstract class AirspaceRouteHoldAltitude {

    @XmlSchemaType(name = "integer")
    protected Integer altitude;
    @XmlElement(defaultValue = "false")
    protected Boolean isMsl;
    @XmlElement(defaultValue = "false")
    protected Boolean isNotam;
    @XmlElement(defaultValue = "false")
    protected Boolean isNotSpecified;
    @XmlElement(defaultValue = "false")
    protected Boolean isFlightLevel;
    @XmlElement(defaultValue = "false")
    protected Boolean isUnknown;

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAltitude(Integer value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the isMsl property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMsl() {
        return isMsl;
    }

    /**
     * Sets the value of the isMsl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMsl(Boolean value) {
        this.isMsl = value;
    }

    /**
     * Gets the value of the isNotam property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNotam() {
        return isNotam;
    }

    /**
     * Sets the value of the isNotam property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNotam(Boolean value) {
        this.isNotam = value;
    }

    /**
     * Gets the value of the isNotSpecified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNotSpecified() {
        return isNotSpecified;
    }

    /**
     * Sets the value of the isNotSpecified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNotSpecified(Boolean value) {
        this.isNotSpecified = value;
    }

    /**
     * Gets the value of the isFlightLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFlightLevel() {
        return isFlightLevel;
    }

    /**
     * Sets the value of the isFlightLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFlightLevel(Boolean value) {
        this.isFlightLevel = value;
    }

    /**
     * Gets the value of the isUnknown property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnknown() {
        return isUnknown;
    }

    /**
     * Sets the value of the isUnknown property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnknown(Boolean value) {
        this.isUnknown = value;
    }

}
