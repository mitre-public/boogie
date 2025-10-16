
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 The offset (from a navaid or waypoint)
 *                 is specified by bearing in degrees to
 *                 the navaid/waypoint and distance in
 *                 nautical miles.
 *             
 * 
 * <p>Java class for Offset complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Offset"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}bearingInMagneticDegrees"/&gt;
 *         &lt;element ref="{}distanceInNauticalMiles"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Offset", propOrder = {
    "bearingInMagneticDegrees",
    "distanceInNauticalMiles"
})
public class Offset {

    @XmlElement(required = true)
    protected AirRefuelingFixBearing bearingInMagneticDegrees;
    @XmlElement(required = true)
    protected AirRefuelingFixDistance distanceInNauticalMiles;

    /**
     * Gets the value of the bearingInMagneticDegrees property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingFixBearing }
     *     
     */
    public AirRefuelingFixBearing getBearingInMagneticDegrees() {
        return bearingInMagneticDegrees;
    }

    /**
     * Sets the value of the bearingInMagneticDegrees property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingFixBearing }
     *     
     */
    public void setBearingInMagneticDegrees(AirRefuelingFixBearing value) {
        this.bearingInMagneticDegrees = value;
    }

    /**
     * Gets the value of the distanceInNauticalMiles property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingFixDistance }
     *     
     */
    public AirRefuelingFixDistance getDistanceInNauticalMiles() {
        return distanceInNauticalMiles;
    }

    /**
     * Sets the value of the distanceInNauticalMiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingFixDistance }
     *     
     */
    public void setDistanceInNauticalMiles(AirRefuelingFixDistance value) {
        this.distanceInNauticalMiles = value;
    }

}
