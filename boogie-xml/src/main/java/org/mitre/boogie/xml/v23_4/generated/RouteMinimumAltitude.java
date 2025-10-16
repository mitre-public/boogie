
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Minimum Alitude (5.30) for the enroute airway leg, preferred route leg and holding pattern. The altitude element will contain the boolean value to represent the legacy ARINC alpha characters; e.g. isUnknown when the MEA/MFA is unknown ("UNKNN"), or isNotSpecified when the MEA/MFA has not been established by the appropriate authority ("NESTB").
 * 
 * <p>Java class for RouteMinimumAltitude complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RouteMinimumAltitude"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}AirspaceRouteHoldAltitude"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altitudeDirectionRestriction" type="{http://www.arinc424.com/xml/enumerations}EnrouteAirwayDirectionalRestriction" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteMinimumAltitude", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "altitudeDirectionRestriction"
})
public class RouteMinimumAltitude
    extends AirspaceRouteHoldAltitude
{

    @XmlSchemaType(name = "string")
    protected EnrouteAirwayDirectionalRestriction altitudeDirectionRestriction;

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

}
