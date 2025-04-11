
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Special Activity Area Operating Times field contains the annual expected operation schedule of the Special Activity.
 * 
 * <p>Java class for SpecialActivityAreaOperatingTimes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpecialActivityAreaOperatingTimes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Alpha"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="holidayQualifier" type="{http://www.arinc424.com/xml/enumerations}HolidayQualifier"/&gt;
 *         &lt;element name="holidays" type="{http://www.arinc424.com/xml/enumerations}Holidays"/&gt;
 *         &lt;element name="timeOfUse" type="{http://www.arinc424.com/xml/enumerations}TimeOfUse"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpecialActivityAreaOperatingTimes", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "holidayQualifier",
    "holidays",
    "timeOfUse"
})
public class SpecialActivityAreaOperatingTimes
    extends Alpha
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HolidayQualifier holidayQualifier;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Holidays holidays;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TimeOfUse timeOfUse;

    /**
     * Gets the value of the holidayQualifier property.
     * 
     * @return
     *     possible object is
     *     {@link HolidayQualifier }
     *     
     */
    public HolidayQualifier getHolidayQualifier() {
        return holidayQualifier;
    }

    /**
     * Sets the value of the holidayQualifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link HolidayQualifier }
     *     
     */
    public void setHolidayQualifier(HolidayQualifier value) {
        this.holidayQualifier = value;
    }

    /**
     * Gets the value of the holidays property.
     * 
     * @return
     *     possible object is
     *     {@link Holidays }
     *     
     */
    public Holidays getHolidays() {
        return holidays;
    }

    /**
     * Sets the value of the holidays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Holidays }
     *     
     */
    public void setHolidays(Holidays value) {
        this.holidays = value;
    }

    /**
     * Gets the value of the timeOfUse property.
     * 
     * @return
     *     possible object is
     *     {@link TimeOfUse }
     *     
     */
    public TimeOfUse getTimeOfUse() {
        return timeOfUse;
    }

    /**
     * Sets the value of the timeOfUse property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeOfUse }
     *     
     */
    public void setTimeOfUse(TimeOfUse value) {
        this.timeOfUse = value;
    }

}
