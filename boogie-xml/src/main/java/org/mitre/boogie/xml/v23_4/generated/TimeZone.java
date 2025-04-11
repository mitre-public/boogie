
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The standard time zone system is based on the division of world into 24 zones, each of 15 degrees longitude. The “zero” time zone is entered at Greenwich meridian with longitudes 7 degrees, 30 minutes West and 7 degrees, 30 minutes east, and there is no difference in the standard time of this time zone and Greenwich Mean Time. Time zones are designated by an integer offset from Greenwich Mean Time, which represets the time difference from GMT in hours, and a minute offset for non-standard time zone offsets.  (See ARINC 424 5.178 for further explanation.)
 * 
 * <p>Java class for TimeZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeZone"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="hourOffset"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;minInclusive value="-14"/&gt;
 *               &lt;maxInclusive value="12"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="minuteOffset" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeZone", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "hourOffset",
    "minuteOffset"
})
public class TimeZone {

    protected int hourOffset;
    protected int minuteOffset;

    /**
     * Gets the value of the hourOffset property.
     * 
     */
    public int getHourOffset() {
        return hourOffset;
    }

    /**
     * Sets the value of the hourOffset property.
     * 
     */
    public void setHourOffset(int value) {
        this.hourOffset = value;
    }

    /**
     * Gets the value of the minuteOffset property.
     * 
     */
    public int getMinuteOffset() {
        return minuteOffset;
    }

    /**
     * Sets the value of the minuteOffset property.
     * 
     */
    public void setMinuteOffset(int value) {
        this.minuteOffset = value;
    }

}
