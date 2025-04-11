
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * When government source provides a speed restriction then this element will be provided
 * 
 * <p>Java class for AirspaceSpeedRestriction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirspaceSpeedRestriction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="speedRestriction" type="{http://www.arinc424.com/xml/datatypes}SpeedLimit"/&gt;
 *         &lt;element name="speedAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirspaceSpeedRestriction", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "speedRestriction",
    "speedAltitude"
})
public class AirspaceSpeedRestriction {

    @XmlElement(required = true)
    protected SpeedLimit speedRestriction;
    @XmlSchemaType(name = "integer")
    protected int speedAltitude;

    /**
     * Gets the value of the speedRestriction property.
     * 
     * @return
     *     possible object is
     *     {@link SpeedLimit }
     *     
     */
    public SpeedLimit getSpeedRestriction() {
        return speedRestriction;
    }

    /**
     * Sets the value of the speedRestriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpeedLimit }
     *     
     */
    public void setSpeedRestriction(SpeedLimit value) {
        this.speedRestriction = value;
    }

    /**
     * Gets the value of the speedAltitude property.
     * 
     */
    public int getSpeedAltitude() {
        return speedAltitude;
    }

    /**
     * Sets the value of the speedAltitude property.
     * 
     */
    public void setSpeedAltitude(int value) {
        this.speedAltitude = value;
    }

}
