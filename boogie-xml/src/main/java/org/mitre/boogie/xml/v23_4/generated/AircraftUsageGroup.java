
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Aircraft Use Group field provides information on what aircraft or groups of aircraft are permitted to use a certain route.
 * 
 * <p>Java class for AircraftUsageGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AircraftUsageGroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Alpha"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="aircraftOrAircraftGroup" type="{http://www.arinc424.com/xml/enumerations}AircraftOrAircraftGroup"/&gt;
 *         &lt;element name="alternateAircraftOrAircraftGroup" type="{http://www.arinc424.com/xml/datatypes}AircraftUsageFieldContent2"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AircraftUsageGroup", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "aircraftOrAircraftGroup",
    "alternateAircraftOrAircraftGroup"
})
public class AircraftUsageGroup
    extends Alpha
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AircraftOrAircraftGroup aircraftOrAircraftGroup;
    @XmlElement(required = true)
    protected String alternateAircraftOrAircraftGroup;

    /**
     * Gets the value of the aircraftOrAircraftGroup property.
     * 
     * @return
     *     possible object is
     *     {@link AircraftOrAircraftGroup }
     *     
     */
    public AircraftOrAircraftGroup getAircraftOrAircraftGroup() {
        return aircraftOrAircraftGroup;
    }

    /**
     * Sets the value of the aircraftOrAircraftGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link AircraftOrAircraftGroup }
     *     
     */
    public void setAircraftOrAircraftGroup(AircraftOrAircraftGroup value) {
        this.aircraftOrAircraftGroup = value;
    }

    /**
     * Gets the value of the alternateAircraftOrAircraftGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateAircraftOrAircraftGroup() {
        return alternateAircraftOrAircraftGroup;
    }

    /**
     * Sets the value of the alternateAircraftOrAircraftGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateAircraftOrAircraftGroup(String value) {
        this.alternateAircraftOrAircraftGroup = value;
    }

}
