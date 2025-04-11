
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Waypoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Waypoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nameFormatIndicator" type="{http://www.arinc424.com/xml/enumerations}NameFormatIndicator" minOccurs="0"/&gt;
 *         &lt;element name="waypointType" type="{http://www.arinc424.com/xml/datatypes}WaypointType"/&gt;
 *         &lt;element name="waypointUsage" type="{http://www.arinc424.com/xml/datatypes}WaypointUsage"/&gt;
 *         &lt;element name="fraInfo" type="{http://www.arinc424.com/xml/datatypes}FraInfo" minOccurs="0"/&gt;
 *         &lt;element name="isVFRCheckpoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Waypoint", propOrder = {
    "nameFormatIndicator",
    "waypointType",
    "waypointUsage",
    "fraInfo",
    "isVFRCheckpoint"
})
@XmlSeeAlso({
    TerminalWaypoint.class
})
public class Waypoint
    extends A424Point
{

    @XmlSchemaType(name = "string")
    protected NameFormatIndicator nameFormatIndicator;
    @XmlElement(required = true)
    protected WaypointType waypointType;
    @XmlElement(required = true)
    protected WaypointUsage waypointUsage;
    protected FraInfo fraInfo;
    @XmlElement(defaultValue = "false")
    protected Boolean isVFRCheckpoint;

    /**
     * Gets the value of the nameFormatIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link NameFormatIndicator }
     *     
     */
    public NameFormatIndicator getNameFormatIndicator() {
        return nameFormatIndicator;
    }

    /**
     * Sets the value of the nameFormatIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameFormatIndicator }
     *     
     */
    public void setNameFormatIndicator(NameFormatIndicator value) {
        this.nameFormatIndicator = value;
    }

    /**
     * Gets the value of the waypointType property.
     * 
     * @return
     *     possible object is
     *     {@link WaypointType }
     *     
     */
    public WaypointType getWaypointType() {
        return waypointType;
    }

    /**
     * Sets the value of the waypointType property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaypointType }
     *     
     */
    public void setWaypointType(WaypointType value) {
        this.waypointType = value;
    }

    /**
     * Gets the value of the waypointUsage property.
     * 
     * @return
     *     possible object is
     *     {@link WaypointUsage }
     *     
     */
    public WaypointUsage getWaypointUsage() {
        return waypointUsage;
    }

    /**
     * Sets the value of the waypointUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaypointUsage }
     *     
     */
    public void setWaypointUsage(WaypointUsage value) {
        this.waypointUsage = value;
    }

    /**
     * Gets the value of the fraInfo property.
     * 
     * @return
     *     possible object is
     *     {@link FraInfo }
     *     
     */
    public FraInfo getFraInfo() {
        return fraInfo;
    }

    /**
     * Sets the value of the fraInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FraInfo }
     *     
     */
    public void setFraInfo(FraInfo value) {
        this.fraInfo = value;
    }

    /**
     * Gets the value of the isVFRCheckpoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVFRCheckpoint() {
        return isVFRCheckpoint;
    }

    /**
     * Sets the value of the isVFRCheckpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVFRCheckpoint(Boolean value) {
        this.isVFRCheckpoint = value;
    }

}
