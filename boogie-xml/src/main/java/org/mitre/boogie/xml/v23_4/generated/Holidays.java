
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Holidays.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Holidays"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Continuous"/&gt;
 *     &lt;enumeration value="Weekdays"/&gt;
 *     &lt;enumeration value="Weekends"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="WeekendsAndHolidays"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Holidays", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum Holidays {


    /**
     * Continuous
     * 
     */
    @XmlEnumValue("Continuous")
    CONTINUOUS("Continuous"),

    /**
     * Weekdays
     * 
     */
    @XmlEnumValue("Weekdays")
    WEEKDAYS("Weekdays"),

    /**
     * Weekends
     * 
     */
    @XmlEnumValue("Weekends")
    WEEKENDS("Weekends"),

    /**
     * Other
     * 
     */
    @XmlEnumValue("Other")
    OTHER("Other"),

    /**
     * Unknown
     * 
     */
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),

    /**
     * Weekends and holidays
     * 
     */
    @XmlEnumValue("WeekendsAndHolidays")
    WEEKENDS_AND_HOLIDAYS("WeekendsAndHolidays");
    private final String value;

    Holidays(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Holidays fromValue(String v) {
        for (Holidays c: Holidays.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
