
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Airspace Record.
 * 
 * <p>Java class for AirspaceSegment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirspaceSegment"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="arcBearing" type="{http://www.arinc424.com/xml/datatypes}ArcBearing" minOccurs="0"/&gt;
 *         &lt;element name="arcDistance" type="{http://www.arinc424.com/xml/datatypes}ArcDistance" minOccurs="0"/&gt;
 *         &lt;element name="arcOriginLocation" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *         &lt;element name="boundaryVia" type="{http://www.arinc424.com/xml/enumerations}BoundaryVia"/&gt;
 *         &lt;element name="isEndOfDescription" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="level" type="{http://www.arinc424.com/xml/enumerations}Level" minOccurs="0"/&gt;
 *         &lt;element name="location" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *         &lt;element name="sequenceNumber" type="{http://www.arinc424.com/xml/datatypes}SequenceNumber"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirspaceSegment", propOrder = {
    "arcBearing",
    "arcDistance",
    "arcOriginLocation",
    "boundaryVia",
    "isEndOfDescription",
    "level",
    "location",
    "sequenceNumber"
})
@XmlSeeAlso({
    FirUirSegment.class
})
public class AirspaceSegment
    extends A424Record
{

    protected BigDecimal arcBearing;
    protected BigDecimal arcDistance;
    protected Location arcOriginLocation;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected BoundaryVia boundaryVia;
    @XmlElement(defaultValue = "false")
    protected Boolean isEndOfDescription;
    @XmlSchemaType(name = "string")
    protected Level level;
    protected Location location;
    @XmlSchemaType(name = "unsignedInt")
    protected long sequenceNumber;

    /**
     * Gets the value of the arcBearing property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getArcBearing() {
        return arcBearing;
    }

    /**
     * Sets the value of the arcBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setArcBearing(BigDecimal value) {
        this.arcBearing = value;
    }

    /**
     * Gets the value of the arcDistance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getArcDistance() {
        return arcDistance;
    }

    /**
     * Sets the value of the arcDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setArcDistance(BigDecimal value) {
        this.arcDistance = value;
    }

    /**
     * Gets the value of the arcOriginLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getArcOriginLocation() {
        return arcOriginLocation;
    }

    /**
     * Sets the value of the arcOriginLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setArcOriginLocation(Location value) {
        this.arcOriginLocation = value;
    }

    /**
     * Gets the value of the boundaryVia property.
     * 
     * @return
     *     possible object is
     *     {@link BoundaryVia }
     *     
     */
    public BoundaryVia getBoundaryVia() {
        return boundaryVia;
    }

    /**
     * Sets the value of the boundaryVia property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundaryVia }
     *     
     */
    public void setBoundaryVia(BoundaryVia value) {
        this.boundaryVia = value;
    }

    /**
     * Gets the value of the isEndOfDescription property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEndOfDescription() {
        return isEndOfDescription;
    }

    /**
     * Sets the value of the isEndOfDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEndOfDescription(Boolean value) {
        this.isEndOfDescription = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Level }
     *     
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Level }
     *     
     */
    public void setLevel(Level value) {
        this.level = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    public void setSequenceNumber(long value) {
        this.sequenceNumber = value;
    }

}
