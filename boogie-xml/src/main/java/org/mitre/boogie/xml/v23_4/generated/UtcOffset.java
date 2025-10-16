
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This type specifies the time zone for a port, and whether the port observes daylight time.
 * 
 * <p>Java class for UtcOffset complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UtcOffset"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="timeZone" type="{http://www.arinc424.com/xml/datatypes}TimeZone" minOccurs="0"/&gt;
 *         &lt;element name="isDaylightObserving" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UtcOffset", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "timeZone",
    "isDaylightObserving"
})
public class UtcOffset {

    protected TimeZone timeZone;
    @XmlElement(defaultValue = "false")
    protected Boolean isDaylightObserving;

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link TimeZone }
     *     
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeZone }
     *     
     */
    public void setTimeZone(TimeZone value) {
        this.timeZone = value;
    }

    /**
     * Gets the value of the isDaylightObserving property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDaylightObserving() {
        return isDaylightObserving;
    }

    /**
     * Sets the value of the isDaylightObserving property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDaylightObserving(Boolean value) {
        this.isDaylightObserving = value;
    }

}
