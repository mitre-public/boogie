
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Special Activity Area (SAA) file contains details relating to operation that could be hazardous to aeronautical navigation around a specified location
 * 
 * <p>Java class for SpecialActivityArea complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecialActivityArea"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="activityType" type="{http://www.arinc424.com/xml/enumerations}ActivityType"/&gt;
 *         &lt;element name="communicationType" type="{http://www.arinc424.com/xml/enumerations}CommunicationType"/&gt;
 *         &lt;element name="communicationFrequency" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="controllingAgency" type="{http://www.arinc424.com/xml/datatypes}ControllingAgency"/&gt;
 *         &lt;element name="icaoCode" type="{http://www.arinc424.com/xml/datatypes}IcaoCode"/&gt;
 *         &lt;element name="location" type="{http://www.arinc424.com/xml/datatypes}Location"/&gt;
 *         &lt;element name="publicMilitaryIndicator" type="{http://www.arinc424.com/xml/enumerations}PublicMilitaryIndicator"/&gt;
 *         &lt;element name="saaIdentifier" type="{http://www.arinc424.com/xml/datatypes}SaaIdentifier"/&gt;
 *         &lt;element name="saaOperatingTimes" type="{http://www.arinc424.com/xml/datatypes}SpecialActivityAreaOperatingTimes"/&gt;
 *         &lt;element name="saaSize" type="{http://www.arinc424.com/xml/datatypes}SpecialActivityAreaSize"/&gt;
 *         &lt;element name="saaVolume" type="{http://www.arinc424.com/xml/datatypes}SpecialActivityAreaVolume"/&gt;
 *         &lt;element name="specialActivityAreaName" type="{http://www.arinc424.com/xml/datatypes}Name"/&gt;
 *         &lt;element name="unitIndicator" type="{http://www.arinc424.com/xml/enumerations}UnitIndicator"/&gt;
 *         &lt;element name="upperLimit" type="{http://www.arinc424.com/xml/datatypes}UpperLimitConstraint" minOccurs="0"/&gt;
 *         &lt;element name="frequencyUnits" type="{http://www.arinc424.com/xml/enumerations}FrequencyUnits" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpecialActivityArea", propOrder = {
    "activityType",
    "communicationType",
    "communicationFrequency",
    "controllingAgency",
    "icaoCode",
    "location",
    "publicMilitaryIndicator",
    "saaIdentifier",
    "saaOperatingTimes",
    "saaSize",
    "saaVolume",
    "specialActivityAreaName",
    "unitIndicator",
    "upperLimit",
    "frequencyUnits"
})
public class SpecialActivityArea
    extends A424Record
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ActivityType activityType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CommunicationType communicationType;
    @XmlElement(required = true)
    protected String communicationFrequency;
    @XmlElement(required = true)
    protected String controllingAgency;
    @XmlElement(required = true)
    protected String icaoCode;
    @XmlElement(required = true)
    protected Location location;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PublicMilitaryIndicator publicMilitaryIndicator;
    @XmlElement(required = true)
    protected String saaIdentifier;
    @XmlElement(required = true)
    protected SpecialActivityAreaOperatingTimes saaOperatingTimes;
    @XmlSchemaType(name = "unsignedInt")
    protected long saaSize;
    @XmlElement(required = true)
    protected String saaVolume;
    @XmlElement(required = true)
    protected String specialActivityAreaName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected UnitIndicator unitIndicator;
    protected UpperLimitConstraint upperLimit;
    @XmlSchemaType(name = "string")
    protected FrequencyUnits frequencyUnits;

    /**
     * Gets the value of the activityType property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityType }
     *     
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * Sets the value of the activityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityType }
     *     
     */
    public void setActivityType(ActivityType value) {
        this.activityType = value;
    }

    /**
     * Gets the value of the communicationType property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationType }
     *     
     */
    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    /**
     * Sets the value of the communicationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationType }
     *     
     */
    public void setCommunicationType(CommunicationType value) {
        this.communicationType = value;
    }

    /**
     * Gets the value of the communicationFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommunicationFrequency() {
        return communicationFrequency;
    }

    /**
     * Sets the value of the communicationFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommunicationFrequency(String value) {
        this.communicationFrequency = value;
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
     * Gets the value of the icaoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcaoCode() {
        return icaoCode;
    }

    /**
     * Sets the value of the icaoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcaoCode(String value) {
        this.icaoCode = value;
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
     * Gets the value of the publicMilitaryIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link PublicMilitaryIndicator }
     *     
     */
    public PublicMilitaryIndicator getPublicMilitaryIndicator() {
        return publicMilitaryIndicator;
    }

    /**
     * Sets the value of the publicMilitaryIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicMilitaryIndicator }
     *     
     */
    public void setPublicMilitaryIndicator(PublicMilitaryIndicator value) {
        this.publicMilitaryIndicator = value;
    }

    /**
     * Gets the value of the saaIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaaIdentifier() {
        return saaIdentifier;
    }

    /**
     * Sets the value of the saaIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaaIdentifier(String value) {
        this.saaIdentifier = value;
    }

    /**
     * Gets the value of the saaOperatingTimes property.
     * 
     * @return
     *     possible object is
     *     {@link SpecialActivityAreaOperatingTimes }
     *     
     */
    public SpecialActivityAreaOperatingTimes getSaaOperatingTimes() {
        return saaOperatingTimes;
    }

    /**
     * Sets the value of the saaOperatingTimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpecialActivityAreaOperatingTimes }
     *     
     */
    public void setSaaOperatingTimes(SpecialActivityAreaOperatingTimes value) {
        this.saaOperatingTimes = value;
    }

    /**
     * Gets the value of the saaSize property.
     * 
     */
    public long getSaaSize() {
        return saaSize;
    }

    /**
     * Sets the value of the saaSize property.
     * 
     */
    public void setSaaSize(long value) {
        this.saaSize = value;
    }

    /**
     * Gets the value of the saaVolume property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaaVolume() {
        return saaVolume;
    }

    /**
     * Sets the value of the saaVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaaVolume(String value) {
        this.saaVolume = value;
    }

    /**
     * Gets the value of the specialActivityAreaName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialActivityAreaName() {
        return specialActivityAreaName;
    }

    /**
     * Sets the value of the specialActivityAreaName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialActivityAreaName(String value) {
        this.specialActivityAreaName = value;
    }

    /**
     * Gets the value of the unitIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link UnitIndicator }
     *     
     */
    public UnitIndicator getUnitIndicator() {
        return unitIndicator;
    }

    /**
     * Sets the value of the unitIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitIndicator }
     *     
     */
    public void setUnitIndicator(UnitIndicator value) {
        this.unitIndicator = value;
    }

    /**
     * Gets the value of the upperLimit property.
     * 
     * @return
     *     possible object is
     *     {@link UpperLimitConstraint }
     *     
     */
    public UpperLimitConstraint getUpperLimit() {
        return upperLimit;
    }

    /**
     * Sets the value of the upperLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpperLimitConstraint }
     *     
     */
    public void setUpperLimit(UpperLimitConstraint value) {
        this.upperLimit = value;
    }

    /**
     * Gets the value of the frequencyUnits property.
     * 
     * @return
     *     possible object is
     *     {@link FrequencyUnits }
     *     
     */
    public FrequencyUnits getFrequencyUnits() {
        return frequencyUnits;
    }

    /**
     * Sets the value of the frequencyUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrequencyUnits }
     *     
     */
    public void setFrequencyUnits(FrequencyUnits value) {
        this.frequencyUnits = value;
    }

}
