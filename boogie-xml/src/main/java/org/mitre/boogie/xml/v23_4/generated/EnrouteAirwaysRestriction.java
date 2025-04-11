
package org.mitre.boogie.xml.v23_4.generated;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Enroute Airway Restriction.
 * 
 * <p>Java class for EnrouteAirwaysRestriction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnrouteAirwaysRestriction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="startFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="endFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="isEveryYear" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="restrictionIdentifier" type="{http://www.arinc424.com/xml/datatypes}RestrictionIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="timesOfOperation" type="{http://www.arinc424.com/xml/datatypes}TimesOfOperation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnrouteAirwaysRestriction", propOrder = {
    "startFixRef",
    "endFixRef",
    "startDate",
    "endDate",
    "isEveryYear",
    "restrictionIdentifier",
    "timesOfOperation"
})
@XmlSeeAlso({
    EnrouteAirwaysRestrictionAltitudeExclusion.class,
    EnrouteAirwaysRestrictionSeasonalClosure.class,
    EnrouteAirwaysRestrictionCruisingTableReplacement.class,
    EnrouteAirwaysRestrictionNote.class
})
public abstract class EnrouteAirwaysRestriction
    extends A424Record
{

    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object startFixRef;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object endFixRef;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(defaultValue = "false")
    protected Boolean isEveryYear;
    @XmlSchemaType(name = "unsignedInt")
    protected Long restrictionIdentifier;
    protected TimesOfOperation timesOfOperation;

    /**
     * Gets the value of the startFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getStartFixRef() {
        return startFixRef;
    }

    /**
     * Sets the value of the startFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setStartFixRef(Object value) {
        this.startFixRef = value;
    }

    /**
     * Gets the value of the endFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEndFixRef() {
        return endFixRef;
    }

    /**
     * Sets the value of the endFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEndFixRef(Object value) {
        this.endFixRef = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the isEveryYear property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEveryYear() {
        return isEveryYear;
    }

    /**
     * Sets the value of the isEveryYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEveryYear(Boolean value) {
        this.isEveryYear = value;
    }

    /**
     * Gets the value of the restrictionIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRestrictionIdentifier() {
        return restrictionIdentifier;
    }

    /**
     * Sets the value of the restrictionIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRestrictionIdentifier(Long value) {
        this.restrictionIdentifier = value;
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

}
