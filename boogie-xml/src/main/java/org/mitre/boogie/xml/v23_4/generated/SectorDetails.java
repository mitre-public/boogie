
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction used to represent the details for a Sector.
 * 
 * <p>Java class for SectorDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SectorDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altitudeLimitation" type="{http://www.arinc424.com/xml/datatypes}AltitudeLimitation"/&gt;
 *         &lt;element name="distanceDescription" type="{http://www.arinc424.com/xml/enumerations}DistanceDescription"/&gt;
 *         &lt;element name="distanceLimitationBegin" type="{http://www.arinc424.com/xml/datatypes}DistanceIntegerNM"/&gt;
 *         &lt;element name="distanceLimitationEnd" type="{http://www.arinc424.com/xml/datatypes}DistanceIntegerNM"/&gt;
 *         &lt;element name="sectorFrom" type="{http://www.arinc424.com/xml/enumerations}SectorCodes"/&gt;
 *         &lt;element name="sectorTo" type="{http://www.arinc424.com/xml/enumerations}SectorCodes"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SectorDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "altitudeLimitation",
    "distanceDescription",
    "distanceLimitationBegin",
    "distanceLimitationEnd",
    "sectorFrom",
    "sectorTo"
})
public class SectorDetails {

    @XmlElement(required = true)
    protected AltitudeLimitation altitudeLimitation;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected DistanceDescription distanceDescription;
    @XmlSchemaType(name = "unsignedInt")
    protected long distanceLimitationBegin;
    @XmlSchemaType(name = "unsignedInt")
    protected long distanceLimitationEnd;
    @XmlElement(required = true)
    protected String sectorFrom;
    @XmlElement(required = true)
    protected String sectorTo;

    /**
     * Gets the value of the altitudeLimitation property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeLimitation }
     *     
     */
    public AltitudeLimitation getAltitudeLimitation() {
        return altitudeLimitation;
    }

    /**
     * Sets the value of the altitudeLimitation property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeLimitation }
     *     
     */
    public void setAltitudeLimitation(AltitudeLimitation value) {
        this.altitudeLimitation = value;
    }

    /**
     * Gets the value of the distanceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link DistanceDescription }
     *     
     */
    public DistanceDescription getDistanceDescription() {
        return distanceDescription;
    }

    /**
     * Sets the value of the distanceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link DistanceDescription }
     *     
     */
    public void setDistanceDescription(DistanceDescription value) {
        this.distanceDescription = value;
    }

    /**
     * Gets the value of the distanceLimitationBegin property.
     * 
     */
    public long getDistanceLimitationBegin() {
        return distanceLimitationBegin;
    }

    /**
     * Sets the value of the distanceLimitationBegin property.
     * 
     */
    public void setDistanceLimitationBegin(long value) {
        this.distanceLimitationBegin = value;
    }

    /**
     * Gets the value of the distanceLimitationEnd property.
     * 
     */
    public long getDistanceLimitationEnd() {
        return distanceLimitationEnd;
    }

    /**
     * Sets the value of the distanceLimitationEnd property.
     * 
     */
    public void setDistanceLimitationEnd(long value) {
        this.distanceLimitationEnd = value;
    }

    /**
     * Gets the value of the sectorFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorFrom() {
        return sectorFrom;
    }

    /**
     * Sets the value of the sectorFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorFrom(String value) {
        this.sectorFrom = value;
    }

    /**
     * Gets the value of the sectorTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorTo() {
        return sectorTo;
    }

    /**
     * Sets the value of the sectorTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorTo(String value) {
        this.sectorTo = value;
    }

}
