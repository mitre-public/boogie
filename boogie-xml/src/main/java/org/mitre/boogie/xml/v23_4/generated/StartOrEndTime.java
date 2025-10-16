
package org.mitre.boogie.xml.v23_4.generated;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StartOrEndTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StartOrEndTime"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dayOfWeek" type="{http://www.arinc424.com/xml/enumerations}DayOfWeek" minOccurs="0"/&gt;
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/&gt;
 *         &lt;element name="relativeTimeIndicator" type="{http://www.arinc424.com/xml/enumerations}RelativeTimeIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StartOrEndTime", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "dayOfWeek",
    "time",
    "relativeTimeIndicator"
})
public class StartOrEndTime {

    @XmlSchemaType(name = "string")
    protected DayOfWeek dayOfWeek;
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar time;
    @XmlSchemaType(name = "string")
    protected RelativeTimeIndicator relativeTimeIndicator;

    /**
     * Gets the value of the dayOfWeek property.
     * 
     * @return
     *     possible object is
     *     {@link DayOfWeek }
     *     
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Sets the value of the dayOfWeek property.
     * 
     * @param value
     *     allowed object is
     *     {@link DayOfWeek }
     *     
     */
    public void setDayOfWeek(DayOfWeek value) {
        this.dayOfWeek = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
    }

    /**
     * Gets the value of the relativeTimeIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link RelativeTimeIndicator }
     *     
     */
    public RelativeTimeIndicator getRelativeTimeIndicator() {
        return relativeTimeIndicator;
    }

    /**
     * Sets the value of the relativeTimeIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeTimeIndicator }
     *     
     */
    public void setRelativeTimeIndicator(RelativeTimeIndicator value) {
        this.relativeTimeIndicator = value;
    }

}
