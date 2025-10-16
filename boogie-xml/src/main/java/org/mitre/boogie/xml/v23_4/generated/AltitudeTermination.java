
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This element contains altitude termination information
 * 
 * <p>Java class for AltitudeTermination complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AltitudeTermination"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="terminationType" type="{http://www.arinc424.com/xml/enumerations}AltitudeTerminationType"/&gt;
 *         &lt;element name="altitude" type="{http://www.arinc424.com/xml/datatypes}Constraint"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AltitudeTermination", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "terminationType",
    "altitude"
})
public class AltitudeTermination {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AltitudeTerminationType terminationType;
    @XmlElement(required = true)
    protected Constraint altitude;

    /**
     * Gets the value of the terminationType property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeTerminationType }
     *     
     */
    public AltitudeTerminationType getTerminationType() {
        return terminationType;
    }

    /**
     * Sets the value of the terminationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeTerminationType }
     *     
     */
    public void setTerminationType(AltitudeTerminationType value) {
        this.terminationType = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Constraint }
     *     
     */
    public Constraint getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Constraint }
     *     
     */
    public void setAltitude(Constraint value) {
        this.altitude = value;
    }

}
