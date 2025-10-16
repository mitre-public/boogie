
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Airspace complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Airspace"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Base"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="airspaceName" type="{http://www.arinc424.com/xml/datatypes}Name"/&gt;
 *         &lt;element name="multipleCode" type="{http://www.arinc424.com/xml/datatypes}MultipleCode" minOccurs="0"/&gt;
 *         &lt;element name="timesOfOperation" type="{http://www.arinc424.com/xml/datatypes}TimesOfOperation" minOccurs="0"/&gt;
 *         &lt;element name="unitIndicatorLower" type="{http://www.arinc424.com/xml/enumerations}UnitIndicator" minOccurs="0"/&gt;
 *         &lt;element name="unitIndicatorUpper" type="{http://www.arinc424.com/xml/enumerations}UnitIndicator" minOccurs="0"/&gt;
 *         &lt;element name="airspaceSegment" type="{}AirspaceSegment" maxOccurs="unbounded"/&gt;
 *         &lt;element name="airspaceAltLimits" type="{http://www.arinc424.com/xml/datatypes}AirspaceAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="controllingAgency" type="{http://www.arinc424.com/xml/datatypes}ControllingAgency" minOccurs="0"/&gt;
 *         &lt;element name="cycleDate" type="{http://www.arinc424.com/xml/datatypes}CycleDate"/&gt;
 *         &lt;element name="isUavOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Airspace", propOrder = {
    "airspaceName",
    "multipleCode",
    "timesOfOperation",
    "unitIndicatorLower",
    "unitIndicatorUpper",
    "airspaceSegment",
    "airspaceAltLimits",
    "controllingAgency",
    "cycleDate",
    "isUavOnly"
})
@XmlSeeAlso({
    RestrictiveAirspace.class,
    ControlledAirspace.class
})
public abstract class Airspace
    extends A424Base
{

    @XmlElement(required = true)
    protected String airspaceName;
    protected String multipleCode;
    protected TimesOfOperation timesOfOperation;
    @XmlSchemaType(name = "string")
    protected UnitIndicator unitIndicatorLower;
    @XmlSchemaType(name = "string")
    protected UnitIndicator unitIndicatorUpper;
    @XmlElement(required = true)
    protected List<AirspaceSegment> airspaceSegment;
    protected AirspaceAltitudeConstraint airspaceAltLimits;
    protected String controllingAgency;
    @XmlElement(required = true)
    protected String cycleDate;
    @XmlElement(defaultValue = "false")
    protected Boolean isUavOnly;

    /**
     * Gets the value of the airspaceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirspaceName() {
        return airspaceName;
    }

    /**
     * Sets the value of the airspaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirspaceName(String value) {
        this.airspaceName = value;
    }

    /**
     * Gets the value of the multipleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultipleCode() {
        return multipleCode;
    }

    /**
     * Sets the value of the multipleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultipleCode(String value) {
        this.multipleCode = value;
    }

    /**
     * Gets the value of the timesOfOperation property.
     * 
     * @return
     *     possible object is
     *     {@link TimesOfOperation }
     *     
     */
    public TimesOfOperation getTimesOfOperation() {
        return timesOfOperation;
    }

    /**
     * Sets the value of the timesOfOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimesOfOperation }
     *     
     */
    public void setTimesOfOperation(TimesOfOperation value) {
        this.timesOfOperation = value;
    }

    /**
     * Gets the value of the unitIndicatorLower property.
     * 
     * @return
     *     possible object is
     *     {@link UnitIndicator }
     *     
     */
    public UnitIndicator getUnitIndicatorLower() {
        return unitIndicatorLower;
    }

    /**
     * Sets the value of the unitIndicatorLower property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitIndicator }
     *     
     */
    public void setUnitIndicatorLower(UnitIndicator value) {
        this.unitIndicatorLower = value;
    }

    /**
     * Gets the value of the unitIndicatorUpper property.
     * 
     * @return
     *     possible object is
     *     {@link UnitIndicator }
     *     
     */
    public UnitIndicator getUnitIndicatorUpper() {
        return unitIndicatorUpper;
    }

    /**
     * Sets the value of the unitIndicatorUpper property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitIndicator }
     *     
     */
    public void setUnitIndicatorUpper(UnitIndicator value) {
        this.unitIndicatorUpper = value;
    }

    /**
     * Gets the value of the airspaceSegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the airspaceSegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAirspaceSegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AirspaceSegment }
     * 
     * 
     */
    public List<AirspaceSegment> getAirspaceSegment() {
        if (airspaceSegment == null) {
            airspaceSegment = new ArrayList<AirspaceSegment>();
        }
        return this.airspaceSegment;
    }

    /**
     * Gets the value of the airspaceAltLimits property.
     * 
     * @return
     *     possible object is
     *     {@link AirspaceAltitudeConstraint }
     *     
     */
    public AirspaceAltitudeConstraint getAirspaceAltLimits() {
        return airspaceAltLimits;
    }

    /**
     * Sets the value of the airspaceAltLimits property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirspaceAltitudeConstraint }
     *     
     */
    public void setAirspaceAltLimits(AirspaceAltitudeConstraint value) {
        this.airspaceAltLimits = value;
    }

    /**
     * Gets the value of the controllingAgency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControllingAgency() {
        return controllingAgency;
    }

    /**
     * Sets the value of the controllingAgency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControllingAgency(String value) {
        this.controllingAgency = value;
    }

    /**
     * Gets the value of the cycleDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCycleDate() {
        return cycleDate;
    }

    /**
     * Sets the value of the cycleDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCycleDate(String value) {
        this.cycleDate = value;
    }

    /**
     * Gets the value of the isUavOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsUavOnly() {
        return isUavOnly;
    }

    /**
     * Sets the value of the isUavOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUavOnly(Boolean value) {
        this.isUavOnly = value;
    }

}
