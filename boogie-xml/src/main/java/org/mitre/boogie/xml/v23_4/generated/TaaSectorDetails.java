
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents the details for a TAA Sector.
 * 
 * <p>Java class for TaaSectorDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaaSectorDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Sector"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sectorRadiusStart" type="{http://www.arinc424.com/xml/datatypes}RadiusLimit"/&gt;
 *         &lt;element name="sectorRadiusEnd" type="{http://www.arinc424.com/xml/datatypes}RadiusLimit"/&gt;
 *         &lt;element name="procedureTurn" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaaSectorDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "sectorRadiusStart",
    "sectorRadiusEnd",
    "procedureTurn"
})
public class TaaSectorDetails
    extends Sector
{

    @XmlSchemaType(name = "unsignedInt")
    protected long sectorRadiusStart;
    @XmlSchemaType(name = "unsignedInt")
    protected long sectorRadiusEnd;
    @XmlElement(defaultValue = "false")
    protected boolean procedureTurn;

    /**
     * Gets the value of the sectorRadiusStart property.
     * 
     */
    public long getSectorRadiusStart() {
        return sectorRadiusStart;
    }

    /**
     * Sets the value of the sectorRadiusStart property.
     * 
     */
    public void setSectorRadiusStart(long value) {
        this.sectorRadiusStart = value;
    }

    /**
     * Gets the value of the sectorRadiusEnd property.
     * 
     */
    public long getSectorRadiusEnd() {
        return sectorRadiusEnd;
    }

    /**
     * Sets the value of the sectorRadiusEnd property.
     * 
     */
    public void setSectorRadiusEnd(long value) {
        this.sectorRadiusEnd = value;
    }

    /**
     * Gets the value of the procedureTurn property.
     * 
     */
    public boolean isProcedureTurn() {
        return procedureTurn;
    }

    /**
     * Sets the value of the procedureTurn property.
     * 
     */
    public void setProcedureTurn(boolean value) {
        this.procedureTurn = value;
    }

}
