
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Minimum and maximum altitudes for a holding pattern and the RVSM minimum/maximum levels for an airway leg or holding pattern.
 * 
 * <p>Java class for HoldRvsmMinimumMaximumAltitudeConstraint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HoldRvsmMinimumMaximumAltitudeConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="minimumAltitude" type="{http://www.arinc424.com/xml/datatypes}AirspaceRouteHoldAltitude" minOccurs="0"/&gt;
 *         &lt;element name="maximumAltitude" type="{http://www.arinc424.com/xml/datatypes}AirspaceRouteHoldAltitude" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HoldRvsmMinimumMaximumAltitudeConstraint", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "minimumAltitude",
    "maximumAltitude"
})
public class HoldRvsmMinimumMaximumAltitudeConstraint {

    protected AirspaceRouteHoldAltitude minimumAltitude;
    protected AirspaceRouteHoldAltitude maximumAltitude;

    /**
     * Gets the value of the minimumAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link AirspaceRouteHoldAltitude }
     *     
     */
    public AirspaceRouteHoldAltitude getMinimumAltitude() {
        return minimumAltitude;
    }

    /**
     * Sets the value of the minimumAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirspaceRouteHoldAltitude }
     *     
     */
    public void setMinimumAltitude(AirspaceRouteHoldAltitude value) {
        this.minimumAltitude = value;
    }

    /**
     * Gets the value of the maximumAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link AirspaceRouteHoldAltitude }
     *     
     */
    public AirspaceRouteHoldAltitude getMaximumAltitude() {
        return maximumAltitude;
    }

    /**
     * Sets the value of the maximumAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirspaceRouteHoldAltitude }
     *     
     */
    public void setMaximumAltitude(AirspaceRouteHoldAltitude value) {
        this.maximumAltitude = value;
    }

}
