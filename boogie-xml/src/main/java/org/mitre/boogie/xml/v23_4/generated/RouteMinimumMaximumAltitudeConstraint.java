
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Minimum and maximum altitudes for a preferred route leg.
 * 
 * <p>Java class for RouteMinimumMaximumAltitudeConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RouteMinimumMaximumAltitudeConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="minimumAltitude" type="{http://www.arinc424.com/xml/datatypes}RouteMinimumAltitude"/&gt;
 *         &lt;element name="maximumAltitude" type="{http://www.arinc424.com/xml/datatypes}RouteMaximumAltitude"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteMinimumMaximumAltitudeConstraint", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "minimumAltitude",
    "maximumAltitude"
})
public class RouteMinimumMaximumAltitudeConstraint {

    @XmlElement(required = true)
    protected RouteMinimumAltitude minimumAltitude;
    @XmlElement(required = true)
    protected RouteMaximumAltitude maximumAltitude;

    /**
     * Gets the value of the minimumAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link RouteMinimumAltitude }
     *     
     */
    public RouteMinimumAltitude getMinimumAltitude() {
        return minimumAltitude;
    }

    /**
     * Sets the value of the minimumAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link RouteMinimumAltitude }
     *     
     */
    public void setMinimumAltitude(RouteMinimumAltitude value) {
        this.minimumAltitude = value;
    }

    /**
     * Gets the value of the maximumAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link RouteMaximumAltitude }
     *     
     */
    public RouteMaximumAltitude getMaximumAltitude() {
        return maximumAltitude;
    }

    /**
     * Sets the value of the maximumAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link RouteMaximumAltitude }
     *     
     */
    public void setMaximumAltitude(RouteMaximumAltitude value) {
        this.maximumAltitude = value;
    }

}
