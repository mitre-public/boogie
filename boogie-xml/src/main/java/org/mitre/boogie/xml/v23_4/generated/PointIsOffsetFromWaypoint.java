
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 A point on an air refueling route may be
 *                 offset from a waypoint. Point to the portion 
 *                 of the XML document containing the complete
 *                 set of information about the waypoint, give
 *                 the offset to that waypoint. Since offset
 *                 information is often unreliable, provide the
 *                 lat/long location of the point.
 *             
 * 
 * <p>Java class for PointIsOffsetFromWaypoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PointIsOffsetFromWaypoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}waypoint"/&gt;
 *         &lt;element ref="{}offsetFromWaypoint"/&gt;
 *         &lt;element ref="{}location"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointIsOffsetFromWaypoint", propOrder = {
    "waypoint",
    "offsetFromWaypoint",
    "location"
})
public class PointIsOffsetFromWaypoint {

    @XmlElement(required = true)
    protected WaypointRefPlusIcaoCode waypoint;
    @XmlElement(required = true)
    protected Offset offsetFromWaypoint;
    @XmlElement(required = true)
    protected Location location;

    /**
     * Gets the value of the waypoint property.
     * 
     * @return
     *     possible object is
     *     {@link WaypointRefPlusIcaoCode }
     *     
     */
    public WaypointRefPlusIcaoCode getWaypoint() {
        return waypoint;
    }

    /**
     * Sets the value of the waypoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaypointRefPlusIcaoCode }
     *     
     */
    public void setWaypoint(WaypointRefPlusIcaoCode value) {
        this.waypoint = value;
    }

    /**
     * Gets the value of the offsetFromWaypoint property.
     * 
     * @return
     *     possible object is
     *     {@link Offset }
     *     
     */
    public Offset getOffsetFromWaypoint() {
        return offsetFromWaypoint;
    }

    /**
     * Sets the value of the offsetFromWaypoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Offset }
     *     
     */
    public void setOffsetFromWaypoint(Offset value) {
        this.offsetFromWaypoint = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

}
