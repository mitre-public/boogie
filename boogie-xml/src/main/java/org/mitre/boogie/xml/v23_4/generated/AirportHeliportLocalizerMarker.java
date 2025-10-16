
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Airport and Heliport Localizer Marker File (PM) contains details of all markers and locators associated with all types of localizers.
 * 
 * <p>Java class for AirportHeliportLocalizerMarker complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirportHeliportLocalizerMarker"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elevation" type="{http://www.arinc424.com/xml/datatypes}Elevation"/&gt;
 *         &lt;element name="localizerRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="locatorClass" type="{}LocatorClass" minOccurs="0"/&gt;
 *         &lt;element name="locatorFacilityCharacteristics" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="locatorFrequency" type="{http://www.arinc424.com/xml/datatypes}Frequency" minOccurs="0"/&gt;
 *         &lt;element name="markerType" type="{http://www.arinc424.com/xml/enumerations}MarkerType" minOccurs="0"/&gt;
 *         &lt;element name="minorAxisBearing" type="{http://www.arinc424.com/xml/datatypes}MinorAxisBearing" minOccurs="0"/&gt;
 *         &lt;element name="runwayIdentifier" type="{http://www.arinc424.com/xml/datatypes}RunwayIdentifier" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirportHeliportLocalizerMarker", propOrder = {
    "elevation",
    "localizerRef",
    "locatorClass",
    "locatorFacilityCharacteristics",
    "locatorFrequency",
    "markerType",
    "minorAxisBearing",
    "runwayIdentifier"
})
public class AirportHeliportLocalizerMarker
    extends A424Point
{

    protected int elevation;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object localizerRef;
    protected LocatorClass locatorClass;
    protected String locatorFacilityCharacteristics;
    protected Frequency locatorFrequency;
    @XmlSchemaType(name = "string")
    protected MarkerType markerType;
    protected BigDecimal minorAxisBearing;
    protected RunwayIdentifier runwayIdentifier;

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
     * Gets the value of the localizerRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getLocalizerRef() {
        return localizerRef;
    }

    /**
     * Sets the value of the localizerRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setLocalizerRef(Object value) {
        this.localizerRef = value;
    }

    /**
     * Gets the value of the locatorClass property.
     * 
     * @return
     *     possible object is
     *     {@link LocatorClass }
     *     
     */
    public LocatorClass getLocatorClass() {
        return locatorClass;
    }

    /**
     * Sets the value of the locatorClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocatorClass }
     *     
     */
    public void setLocatorClass(LocatorClass value) {
        this.locatorClass = value;
    }

    /**
     * Gets the value of the locatorFacilityCharacteristics property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocatorFacilityCharacteristics() {
        return locatorFacilityCharacteristics;
    }

    /**
     * Sets the value of the locatorFacilityCharacteristics property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocatorFacilityCharacteristics(String value) {
        this.locatorFacilityCharacteristics = value;
    }

    /**
     * Gets the value of the locatorFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getLocatorFrequency() {
        return locatorFrequency;
    }

    /**
     * Sets the value of the locatorFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setLocatorFrequency(Frequency value) {
        this.locatorFrequency = value;
    }

    /**
     * Gets the value of the markerType property.
     * 
     * @return
     *     possible object is
     *     {@link MarkerType }
     *     
     */
    public MarkerType getMarkerType() {
        return markerType;
    }

    /**
     * Sets the value of the markerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarkerType }
     *     
     */
    public void setMarkerType(MarkerType value) {
        this.markerType = value;
    }

    /**
     * Gets the value of the minorAxisBearing property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinorAxisBearing() {
        return minorAxisBearing;
    }

    /**
     * Sets the value of the minorAxisBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinorAxisBearing(BigDecimal value) {
        this.minorAxisBearing = value;
    }

    /**
     * Gets the value of the runwayIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link RunwayIdentifier }
     *     
     */
    public RunwayIdentifier getRunwayIdentifier() {
        return runwayIdentifier;
    }

    /**
     * Sets the value of the runwayIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwayIdentifier }
     *     
     */
    public void setRunwayIdentifier(RunwayIdentifier value) {
        this.runwayIdentifier = value;
    }

}
