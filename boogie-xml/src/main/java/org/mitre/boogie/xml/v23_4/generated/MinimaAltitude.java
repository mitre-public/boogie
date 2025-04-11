
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Decision Altitude (DA) or Minimum Descent Altitude (MDA) associated with this landing minima.
 * 
 * <p>Java class for MinimaAltitude complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MinimaAltitude"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altitudeType" type="{http://www.arinc424.com/xml/enumerations}MinimaAltitudeType"/&gt;
 *         &lt;element name="altitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue"/&gt;
 *         &lt;element name="altitudeUom" type="{http://www.arinc424.com/xml/enumerations}HeightUnitsIndicator"/&gt;
 *         &lt;element name="isDerived" type="{http://www.arinc424.com/xml/datatypes}IsDerived" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MinimaAltitude", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "altitudeType",
    "altitude",
    "altitudeUom",
    "isDerived"
})
public class MinimaAltitude {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MinimaAltitudeType altitudeType;
    @XmlSchemaType(name = "integer")
    protected int altitude;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HeightUnitsIndicator altitudeUom;
    @XmlElement(defaultValue = "false")
    protected Boolean isDerived;

    /**
     * Gets the value of the altitudeType property.
     * 
     * @return
     *     possible object is
     *     {@link MinimaAltitudeType }
     *     
     */
    public MinimaAltitudeType getAltitudeType() {
        return altitudeType;
    }

    /**
     * Sets the value of the altitudeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MinimaAltitudeType }
     *     
     */
    public void setAltitudeType(MinimaAltitudeType value) {
        this.altitudeType = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     */
    public int getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     */
    public void setAltitude(int value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the altitudeUom property.
     * 
     * @return
     *     possible object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public HeightUnitsIndicator getAltitudeUom() {
        return altitudeUom;
    }

    /**
     * Sets the value of the altitudeUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public void setAltitudeUom(HeightUnitsIndicator value) {
        this.altitudeUom = value;
    }

    /**
     * Gets the value of the isDerived property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDerived() {
        return isDerived;
    }

    /**
     * Sets the value of the isDerived property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDerived(Boolean value) {
        this.isDerived = value;
    }

}
