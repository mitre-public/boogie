
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Maximum Alitude (5.127) for the airway leg or preferred route leg. The altitude element will contain the boolean value to represent the legacy ARINC alpha characters; e.g. isUnlimited when the MAA/upper limit is unknown or not established.
 * 
 * <p>Java class for RouteMaximumAltitude complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RouteMaximumAltitude"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}AirspaceRouteHoldAltitude"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altitudeDirectionRestriction" type="{http://www.arinc424.com/xml/enumerations}EnrouteAirwayDirectionalRestriction" minOccurs="0"/&gt;
 *         &lt;element name="isUnlimited" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteMaximumAltitude", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "altitudeDirectionRestriction",
    "isUnlimited"
})
public class RouteMaximumAltitude
    extends AirspaceRouteHoldAltitude
{

    @XmlSchemaType(name = "string")
    protected EnrouteAirwayDirectionalRestriction altitudeDirectionRestriction;
    @XmlElement(defaultValue = "false")
    protected Boolean isUnlimited;

    /**
     * Gets the value of the altitudeDirectionRestriction property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteAirwayDirectionalRestriction }
     *     
     */
    public EnrouteAirwayDirectionalRestriction getAltitudeDirectionRestriction() {
        return altitudeDirectionRestriction;
    }

    /**
     * Sets the value of the altitudeDirectionRestriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteAirwayDirectionalRestriction }
     *     
     */
    public void setAltitudeDirectionRestriction(EnrouteAirwayDirectionalRestriction value) {
        this.altitudeDirectionRestriction = value;
    }

    /**
     * Gets the value of the isUnlimited property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUnlimited() {
        return isUnlimited;
    }

    /**
     * Sets the value of the isUnlimited property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUnlimited(Boolean value) {
        this.isUnlimited = value;
    }

}
