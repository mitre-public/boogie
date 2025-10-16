
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HolidayQualifier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="HolidayQualifier"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IncludesHolidays"/&gt;
 *     &lt;enumeration value="ExcludesHolidays"/&gt;
 *     &lt;enumeration value="U"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HolidayQualifier", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum HolidayQualifier {


    /**
     * Including Holidays
     * 
     */
    @XmlEnumValue("IncludesHolidays")
    INCLUDES_HOLIDAYS("IncludesHolidays"),

    /**
     * Excluding Holidays
     * 
     */
    @XmlEnumValue("ExcludesHolidays")
    EXCLUDES_HOLIDAYS("ExcludesHolidays"),

    /**
     * Unknown
     * 
     */
    U("U");
    private final String value;

    HolidayQualifier(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HolidayQualifier fromValue(String v) {
        for (HolidayQualifier c: HolidayQualifier.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
