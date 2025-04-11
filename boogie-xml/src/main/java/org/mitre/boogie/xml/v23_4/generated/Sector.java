
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents the details for a MSA or TAA Sector.
 * 
 * <p>Java class for Sector complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Sector"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sectorAltitude" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/&gt;
 *         &lt;element name="sectorBearingBegin" type="{http://www.arinc424.com/xml/datatypes}SectorBearing"/&gt;
 *         &lt;element name="sectorBearingEnd" type="{http://www.arinc424.com/xml/datatypes}SectorBearing"/&gt;
 *         &lt;element name="sectorRadius" type="{http://www.arinc424.com/xml/datatypes}RadiusLimit"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sector", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "sectorAltitude",
    "sectorBearingBegin",
    "sectorBearingEnd",
    "sectorRadius"
})
@XmlSeeAlso({
    TaaSectorDetails.class
})
public class Sector {

    @XmlSchemaType(name = "unsignedInt")
    protected long sectorAltitude;
    @XmlSchemaType(name = "unsignedInt")
    protected long sectorBearingBegin;
    @XmlSchemaType(name = "unsignedInt")
    protected long sectorBearingEnd;
    @XmlSchemaType(name = "unsignedInt")
    protected long sectorRadius;

    /**
     * Gets the value of the sectorAltitude property.
     * 
     */
    public long getSectorAltitude() {
        return sectorAltitude;
    }

    /**
     * Sets the value of the sectorAltitude property.
     * 
     */
    public void setSectorAltitude(long value) {
        this.sectorAltitude = value;
    }

    /**
     * Gets the value of the sectorBearingBegin property.
     * 
     */
    public long getSectorBearingBegin() {
        return sectorBearingBegin;
    }

    /**
     * Sets the value of the sectorBearingBegin property.
     * 
     */
    public void setSectorBearingBegin(long value) {
        this.sectorBearingBegin = value;
    }

    /**
     * Gets the value of the sectorBearingEnd property.
     * 
     */
    public long getSectorBearingEnd() {
        return sectorBearingEnd;
    }

    /**
     * Sets the value of the sectorBearingEnd property.
     * 
     */
    public void setSectorBearingEnd(long value) {
        this.sectorBearingEnd = value;
    }

    /**
     * Gets the value of the sectorRadius property.
     * 
     */
    public long getSectorRadius() {
        return sectorRadius;
    }

    /**
     * Sets the value of the sectorRadius property.
     * 
     */
    public void setSectorRadius(long value) {
        this.sectorRadius = value;
    }

}
