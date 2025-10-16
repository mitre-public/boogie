
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents the waypoint description information used by procedure legs.
 * 
 * <p>Java class for ProcedureWaypointDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcedureWaypointDescription"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isEssential" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFlyOver" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isHolding" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isNoProcedureTurn" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isPhantomFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isSourceProvidedEnrouteWaypoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isTaaProcedureTurn" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlType(name = "ProcedureWaypointDescription", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "isEssential",
    "isFlyOver",
    "isHolding",
    "isNoProcedureTurn",
    "isPhantomFix",
    "isSourceProvidedEnrouteWaypoint",
    "isTaaProcedureTurn",
    "isAtcCompulsoryReportingPoint"
})
public class ProcedureWaypointDescription {

    @XmlElement(defaultValue = "false")
    protected Boolean isEssential;
    @XmlElement(defaultValue = "false")
    protected Boolean isFlyOver;
    @XmlElement(defaultValue = "false")
    protected Boolean isHolding;
    @XmlElement(defaultValue = "false")
    protected Boolean isNoProcedureTurn;
    @XmlElement(defaultValue = "false")
    protected Boolean isPhantomFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isSourceProvidedEnrouteWaypoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isTaaProcedureTurn;
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
     * Gets the value of the isFlyOver property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFlyOver() {
        return isFlyOver;
    }

    /**
     * Sets the value of the isFlyOver property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFlyOver(Boolean value) {
        this.isFlyOver = value;
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
     * Gets the value of the isNoProcedureTurn property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNoProcedureTurn() {
        return isNoProcedureTurn;
    }

    /**
     * Sets the value of the isNoProcedureTurn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNoProcedureTurn(Boolean value) {
        this.isNoProcedureTurn = value;
    }

    /**
     * Gets the value of the isPhantomFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPhantomFix() {
        return isPhantomFix;
    }

    /**
     * Sets the value of the isPhantomFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPhantomFix(Boolean value) {
        this.isPhantomFix = value;
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
     * Gets the value of the isTaaProcedureTurn property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsTaaProcedureTurn() {
        return isTaaProcedureTurn;
    }

    /**
     * Sets the value of the isTaaProcedureTurn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTaaProcedureTurn(Boolean value) {
        this.isTaaProcedureTurn = value;
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
