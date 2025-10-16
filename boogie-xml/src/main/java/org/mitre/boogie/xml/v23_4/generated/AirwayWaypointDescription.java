
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents the waypoint description information used by airway legs.
 * 
 * <p>Java class for AirwayWaypointDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirwayWaypointDescription"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isEssential" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isHolding" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNonEssential" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOceanicGateway" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isOffAirwayFloating" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isSourceProvidedEnrouteWaypoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTransitionEssential" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isUnchartedIntersection" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isAtcCompulsoryReportingPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirwayWaypointDescription", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isEssential",
    "isHolding",
    "isNonEssential",
    "isOceanicGateway",
    "isOffAirwayFloating",
    "isSourceProvidedEnrouteWaypoint",
    "isTransitionEssential",
    "isUnchartedIntersection",
    "isAtcCompulsoryReportingPoint"
})
public class AirwayWaypointDescription {

    @XmlElement(defaultValue = "false")
    protected Boolean isEssential;
    @XmlElement(defaultValue = "false")
    protected Boolean isHolding;
    @XmlElement(defaultValue = "false")
    protected Boolean isNonEssential;
    @XmlElement(defaultValue = "false")
    protected Boolean isOceanicGateway;
    @XmlElement(defaultValue = "false")
    protected Boolean isOffAirwayFloating;
    @XmlElement(defaultValue = "false")
    protected Boolean isSourceProvidedEnrouteWaypoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isTransitionEssential;
    @XmlElement(defaultValue = "false")
    protected Boolean isUnchartedIntersection;
    @XmlElement(defaultValue = "false")
    protected Boolean isAtcCompulsoryReportingPoint;

    /**
     * Gets the value of the isEssential property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEssential() {
        return isEssential;
    }

    /**
     * Sets the value of the isEssential property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEssential(Boolean value) {
        this.isEssential = value;
    }

    /**
     * Gets the value of the isHolding property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsHolding() {
        return isHolding;
    }

    /**
     * Sets the value of the isHolding property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsHolding(Boolean value) {
        this.isHolding = value;
    }

    /**
     * Gets the value of the isNonEssential property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNonEssential() {
        return isNonEssential;
    }

    /**
     * Sets the value of the isNonEssential property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNonEssential(Boolean value) {
        this.isNonEssential = value;
    }

    /**
     * Gets the value of the isOceanicGateway property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOceanicGateway() {
        return isOceanicGateway;
    }

    /**
     * Sets the value of the isOceanicGateway property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOceanicGateway(Boolean value) {
        this.isOceanicGateway = value;
    }

    /**
     * Gets the value of the isOffAirwayFloating property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOffAirwayFloating() {
        return isOffAirwayFloating;
    }

    /**
     * Sets the value of the isOffAirwayFloating property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOffAirwayFloating(Boolean value) {
        this.isOffAirwayFloating = value;
    }

    /**
     * Gets the value of the isSourceProvidedEnrouteWaypoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSourceProvidedEnrouteWaypoint() {
        return isSourceProvidedEnrouteWaypoint;
    }

    /**
     * Sets the value of the isSourceProvidedEnrouteWaypoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSourceProvidedEnrouteWaypoint(Boolean value) {
        this.isSourceProvidedEnrouteWaypoint = value;
    }

    /**
     * Gets the value of the isTransitionEssential property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTransitionEssential() {
        return isTransitionEssential;
    }

    /**
     * Sets the value of the isTransitionEssential property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTransitionEssential(Boolean value) {
        this.isTransitionEssential = value;
    }

    /**
     * Gets the value of the isUnchartedIntersection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnchartedIntersection() {
        return isUnchartedIntersection;
    }

    /**
     * Sets the value of the isUnchartedIntersection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnchartedIntersection(Boolean value) {
        this.isUnchartedIntersection = value;
    }

    /**
     * Gets the value of the isAtcCompulsoryReportingPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAtcCompulsoryReportingPoint() {
        return isAtcCompulsoryReportingPoint;
    }

    /**
     * Sets the value of the isAtcCompulsoryReportingPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAtcCompulsoryReportingPoint(Boolean value) {
        this.isAtcCompulsoryReportingPoint = value;
    }

}
