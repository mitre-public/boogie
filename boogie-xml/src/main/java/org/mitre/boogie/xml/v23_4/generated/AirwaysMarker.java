
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Airways Marker Records.
 * 
 * <p>Java class for AirwaysMarker complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirwaysMarker"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elevation" type="{http://www.arinc424.com/xml/datatypes}Elevation"/&gt;
 *         &lt;element name="markerCode" type="{http://www.arinc424.com/xml/datatypes}MarkerCode" minOccurs="0"/&gt;
 *         &lt;element name="markerPower" type="{http://www.arinc424.com/xml/enumerations}HighLow" minOccurs="0"/&gt;
 *         &lt;element name="markerShape" type="{http://www.arinc424.com/xml/enumerations}MarkerShape" minOccurs="0"/&gt;
 *         &lt;element name="minorAxis" type="{http://www.arinc424.com/xml/datatypes}MinorAxisBearing" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirwaysMarker", propOrder = {
    "elevation",
    "markerCode",
    "markerPower",
    "markerShape",
    "minorAxis"
})
public class AirwaysMarker
    extends A424Point
{

    protected int elevation;
    protected String markerCode;
    @XmlSchemaType(name = "string")
    protected HighLow markerPower;
    @XmlSchemaType(name = "string")
    protected MarkerShape markerShape;
    protected BigDecimal minorAxis;

    /**
     * Gets the value of the elevation property.
     * 
     */
    public int getElevation() {
        return elevation;
    }

    /**
     * Sets the value of the elevation property.
     * 
     */
    public void setElevation(int value) {
        this.elevation = value;
    }

    /**
     * Gets the value of the markerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarkerCode() {
        return markerCode;
    }

    /**
     * Sets the value of the markerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarkerCode(String value) {
        this.markerCode = value;
    }

    /**
     * Gets the value of the markerPower property.
     * 
     * @return
     *     possible object is
     *     {@link HighLow }
     *     
     */
    public HighLow getMarkerPower() {
        return markerPower;
    }

    /**
     * Sets the value of the markerPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link HighLow }
     *     
     */
    public void setMarkerPower(HighLow value) {
        this.markerPower = value;
    }

    /**
     * Gets the value of the markerShape property.
     * 
     * @return
     *     possible object is
     *     {@link MarkerShape }
     *     
     */
    public MarkerShape getMarkerShape() {
        return markerShape;
    }

    /**
     * Sets the value of the markerShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarkerShape }
     *     
     */
    public void setMarkerShape(MarkerShape value) {
        this.markerShape = value;
    }

    /**
     * Gets the value of the minorAxis property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinorAxis() {
        return minorAxis;
    }

    /**
     * Sets the value of the minorAxis property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinorAxis(BigDecimal value) {
        this.minorAxis = value;
    }

}
