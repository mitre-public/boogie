
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 A point on an air refueling route may be a waypoint.
 *                 Point to the portion of the XML document containing
 *                 the complete set of information about the waypoint.
 *             
 * 
 * <p>Java class for PointIsaWaypoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PointIsaWaypoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}waypoint"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointIsaWaypoint", propOrder = {
    "waypoint"
})
public class PointIsaWaypoint {

    @XmlElement(required = true)
    protected WaypointRefPlusIcaoCode waypoint;

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

}
