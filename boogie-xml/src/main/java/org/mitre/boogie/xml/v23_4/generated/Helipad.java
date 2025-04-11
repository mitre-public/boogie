
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Helipad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Helipad"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Point"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isWithoutLocation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isDerivedLocation" type="{http://www.arinc424.com/xml/datatypes}IsDerived" minOccurs="0"/&gt;
 *         &lt;element name="elevation" type="{http://www.arinc424.com/xml/datatypes}Elevation" minOccurs="0"/&gt;
 *         &lt;element name="elevationType" type="{http://www.arinc424.com/xml/enumerations}ElevationType" minOccurs="0"/&gt;
 *         &lt;element name="helipadTlofDimensions" type="{http://www.arinc424.com/xml/datatypes}HelipadDimensions" minOccurs="0"/&gt;
 *         &lt;element name="helipadFatoDimensions" type="{http://www.arinc424.com/xml/datatypes}HelipadDimensions" minOccurs="0"/&gt;
 *         &lt;element name="safetyDimensions" type="{http://www.arinc424.com/xml/datatypes}HelipadDimensions" minOccurs="0"/&gt;
 *         &lt;element name="helipadShape" type="{http://www.arinc424.com/xml/enumerations}HelipadShape" minOccurs="0"/&gt;
 *         &lt;element name="surfaceCode" type="{http://www.arinc424.com/xml/enumerations}RunwaySurfaceCode" minOccurs="0"/&gt;
 *         &lt;element name="surfaceType" type="{http://www.arinc424.com/xml/enumerations}SurfaceType" minOccurs="0"/&gt;
 *         &lt;element name="helicopterPerformanceReq" type="{http://www.arinc424.com/xml/enumerations}HelicopterPerformanceReq" minOccurs="0"/&gt;
 *         &lt;element name="isElevated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="helipadMaximumRotorDiameter" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="helipadOrientation" type="{http://www.arinc424.com/xml/datatypes}Bearing" minOccurs="0"/&gt;
 *         &lt;element name="helipadIdentifierOrientation" type="{http://www.arinc424.com/xml/datatypes}Bearing" minOccurs="0"/&gt;
 *         &lt;element name="preferredApproachBearing" type="{http://www.arinc424.com/xml/datatypes}Bearing" maxOccurs="2" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Helipad", propOrder = {
    "isWithoutLocation",
    "isDerivedLocation",
    "elevation",
    "elevationType",
    "helipadTlofDimensions",
    "helipadFatoDimensions",
    "safetyDimensions",
    "helipadShape",
    "surfaceCode",
    "surfaceType",
    "helicopterPerformanceReq",
    "isElevated",
    "helipadMaximumRotorDiameter",
    "helipadOrientation",
    "helipadIdentifierOrientation",
    "preferredApproachBearing"
})
public class Helipad
    extends A424Point
{

    @XmlElement(defaultValue = "false")
    protected Boolean isWithoutLocation;
    @XmlElement(defaultValue = "false")
    protected Boolean isDerivedLocation;
    protected Integer elevation;
    @XmlSchemaType(name = "string")
    protected ElevationType elevationType;
    protected HelipadDimensions helipadTlofDimensions;
    protected HelipadDimensions helipadFatoDimensions;
    protected HelipadDimensions safetyDimensions;
    @XmlSchemaType(name = "string")
    protected HelipadShape helipadShape;
    @XmlSchemaType(name = "string")
    protected RunwaySurfaceCode surfaceCode;
    @XmlSchemaType(name = "string")
    protected SurfaceType surfaceType;
    @XmlSchemaType(name = "string")
    protected HelicopterPerformanceReq helicopterPerformanceReq;
    @XmlElement(defaultValue = "false")
    protected Boolean isElevated;
    protected Integer helipadMaximumRotorDiameter;
    protected Bearing helipadOrientation;
    protected Bearing helipadIdentifierOrientation;
    protected List<Bearing> preferredApproachBearing;

    /**
     * Gets the value of the isWithoutLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsWithoutLocation() {
        return isWithoutLocation;
    }

    /**
     * Sets the value of the isWithoutLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsWithoutLocation(Boolean value) {
        this.isWithoutLocation = value;
    }

    /**
     * Gets the value of the isDerivedLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDerivedLocation() {
        return isDerivedLocation;
    }

    /**
     * Sets the value of the isDerivedLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDerivedLocation(Boolean value) {
        this.isDerivedLocation = value;
    }

    /**
     * Gets the value of the elevation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getElevation() {
        return elevation;
    }

    /**
     * Sets the value of the elevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setElevation(Integer value) {
        this.elevation = value;
    }

    /**
     * Gets the value of the elevationType property.
     * 
     * @return
     *     possible object is
     *     {@link ElevationType }
     *     
     */
    public ElevationType getElevationType() {
        return elevationType;
    }

    /**
     * Sets the value of the elevationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElevationType }
     *     
     */
    public void setElevationType(ElevationType value) {
        this.elevationType = value;
    }

    /**
     * Gets the value of the helipadTlofDimensions property.
     * 
     * @return
     *     possible object is
     *     {@link HelipadDimensions }
     *     
     */
    public HelipadDimensions getHelipadTlofDimensions() {
        return helipadTlofDimensions;
    }

    /**
     * Sets the value of the helipadTlofDimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link HelipadDimensions }
     *     
     */
    public void setHelipadTlofDimensions(HelipadDimensions value) {
        this.helipadTlofDimensions = value;
    }

    /**
     * Gets the value of the helipadFatoDimensions property.
     * 
     * @return
     *     possible object is
     *     {@link HelipadDimensions }
     *     
     */
    public HelipadDimensions getHelipadFatoDimensions() {
        return helipadFatoDimensions;
    }

    /**
     * Sets the value of the helipadFatoDimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link HelipadDimensions }
     *     
     */
    public void setHelipadFatoDimensions(HelipadDimensions value) {
        this.helipadFatoDimensions = value;
    }

    /**
     * Gets the value of the safetyDimensions property.
     * 
     * @return
     *     possible object is
     *     {@link HelipadDimensions }
     *     
     */
    public HelipadDimensions getSafetyDimensions() {
        return safetyDimensions;
    }

    /**
     * Sets the value of the safetyDimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link HelipadDimensions }
     *     
     */
    public void setSafetyDimensions(HelipadDimensions value) {
        this.safetyDimensions = value;
    }

    /**
     * Gets the value of the helipadShape property.
     * 
     * @return
     *     possible object is
     *     {@link HelipadShape }
     *     
     */
    public HelipadShape getHelipadShape() {
        return helipadShape;
    }

    /**
     * Sets the value of the helipadShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link HelipadShape }
     *     
     */
    public void setHelipadShape(HelipadShape value) {
        this.helipadShape = value;
    }

    /**
     * Gets the value of the surfaceCode property.
     * 
     * @return
     *     possible object is
     *     {@link RunwaySurfaceCode }
     *     
     */
    public RunwaySurfaceCode getSurfaceCode() {
        return surfaceCode;
    }

    /**
     * Sets the value of the surfaceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwaySurfaceCode }
     *     
     */
    public void setSurfaceCode(RunwaySurfaceCode value) {
        this.surfaceCode = value;
    }

    /**
     * Gets the value of the surfaceType property.
     * 
     * @return
     *     possible object is
     *     {@link SurfaceType }
     *     
     */
    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    /**
     * Sets the value of the surfaceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SurfaceType }
     *     
     */
    public void setSurfaceType(SurfaceType value) {
        this.surfaceType = value;
    }

    /**
     * Gets the value of the helicopterPerformanceReq property.
     * 
     * @return
     *     possible object is
     *     {@link HelicopterPerformanceReq }
     *     
     */
    public HelicopterPerformanceReq getHelicopterPerformanceReq() {
        return helicopterPerformanceReq;
    }

    /**
     * Sets the value of the helicopterPerformanceReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link HelicopterPerformanceReq }
     *     
     */
    public void setHelicopterPerformanceReq(HelicopterPerformanceReq value) {
        this.helicopterPerformanceReq = value;
    }

    /**
     * Gets the value of the isElevated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsElevated() {
        return isElevated;
    }

    /**
     * Sets the value of the isElevated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsElevated(Boolean value) {
        this.isElevated = value;
    }

    /**
     * Gets the value of the helipadMaximumRotorDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHelipadMaximumRotorDiameter() {
        return helipadMaximumRotorDiameter;
    }

    /**
     * Sets the value of the helipadMaximumRotorDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHelipadMaximumRotorDiameter(Integer value) {
        this.helipadMaximumRotorDiameter = value;
    }

    /**
     * Gets the value of the helipadOrientation property.
     * 
     * @return
     *     possible object is
     *     {@link Bearing }
     *     
     */
    public Bearing getHelipadOrientation() {
        return helipadOrientation;
    }

    /**
     * Sets the value of the helipadOrientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bearing }
     *     
     */
    public void setHelipadOrientation(Bearing value) {
        this.helipadOrientation = value;
    }

    /**
     * Gets the value of the helipadIdentifierOrientation property.
     * 
     * @return
     *     possible object is
     *     {@link Bearing }
     *     
     */
    public Bearing getHelipadIdentifierOrientation() {
        return helipadIdentifierOrientation;
    }

    /**
     * Sets the value of the helipadIdentifierOrientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bearing }
     *     
     */
    public void setHelipadIdentifierOrientation(Bearing value) {
        this.helipadIdentifierOrientation = value;
    }

    /**
     * Gets the value of the preferredApproachBearing property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the preferredApproachBearing property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreferredApproachBearing().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bearing }
     * 
     * 
     */
    public List<Bearing> getPreferredApproachBearing() {
        if (preferredApproachBearing == null) {
            preferredApproachBearing = new ArrayList<Bearing>();
        }
        return this.preferredApproachBearing;
    }

}
