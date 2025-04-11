
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Represents beginning and ending bearings for a Communication Sector.
 * 
 * <p>Java class for SectorBearings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SectorBearings"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sectorBearingBegin" type="{http://www.arinc424.com/xml/datatypes}SectorBearing"/&gt;
 *         &lt;element name="sectorBearingEnd" type="{http://www.arinc424.com/xml/datatypes}SectorBearing"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SectorBearings", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "sectorBearingBegin",
    "sectorBearingEnd"
})
public class SectorBearings {

    @XmlSchemaType(name = "unsignedInt")
    protected long sectorBearingBegin;
    @XmlSchemaType(name = "unsignedInt")
    protected long sectorBearingEnd;

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

}
