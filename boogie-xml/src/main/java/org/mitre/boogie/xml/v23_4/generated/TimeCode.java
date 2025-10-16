
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TimeCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ContinuousIncludingHolidays"/&gt;
 *     &lt;enumeration value="ContinuousExcludingHolidays"/&gt;
 *     &lt;enumeration value="SpecifiedExcludingHolidays"/&gt;
 *     &lt;enumeration value="SpecifiedIncludingHolidays"/&gt;
 *     &lt;enumeration value="Complex"/&gt;
 *     &lt;enumeration value="ByNotam"/&gt;
 *     &lt;enumeration value="Unspecified"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TimeCode", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TimeCode {


    /**
     * Active Continuously, including holidays
     * 
     */
    @XmlEnumValue("ContinuousIncludingHolidays")
    CONTINUOUS_INCLUDING_HOLIDAYS("ContinuousIncludingHolidays"),

    /**
     * Active Continuously, excluding holidays
     * 
     */
    @XmlEnumValue("ContinuousExcludingHolidays")
    CONTINUOUS_EXCLUDING_HOLIDAYS("ContinuousExcludingHolidays"),

    /**
     * Active times are provided in Time of Operation record and exclude holidays
     * 
     */
    @XmlEnumValue("SpecifiedExcludingHolidays")
    SPECIFIED_EXCLUDING_HOLIDAYS("SpecifiedExcludingHolidays"),

    /**
     * Active times are provided in Time of Operation record and include holidays
     * 
     */
    @XmlEnumValue("SpecifiedIncludingHolidays")
    SPECIFIED_INCLUDING_HOLIDAYS("SpecifiedIncludingHolidays"),

    /**
     * Activation Times are too complex for Time of Operation format and are provided in the timeNarrative field
     * 
     */
    @XmlEnumValue("Complex")
    COMPLEX("Complex"),

    /**
     * Active times announced by NOTAM
     * 
     */
    @XmlEnumValue("ByNotam")
    BY_NOTAM("ByNotam"),

    /**
     * Active times are not specified in source documentation
     * 
     */
    @XmlEnumValue("Unspecified")
    UNSPECIFIED("Unspecified");
    private final String value;

    TimeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimeCode fromValue(String v) {
        for (TimeCode c: TimeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
