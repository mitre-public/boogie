
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeOfUse.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TimeOfUse"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SrSs"/&gt;
 *     &lt;enumeration value="Night"/&gt;
 *     &lt;enumeration value="Continuous"/&gt;
 *     &lt;enumeration value="ActiveByNotam"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TimeOfUse", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TimeOfUse {


    /**
     * SR-SS
     * 
     */
    @XmlEnumValue("SrSs")
    SR_SS("SrSs"),

    /**
     * Night Use
     * 
     */
    @XmlEnumValue("Night")
    NIGHT("Night"),

    /**
     * Continuous
     * 
     */
    @XmlEnumValue("Continuous")
    CONTINUOUS("Continuous"),

    /**
     * Active by NOTAM
     * 
     */
    @XmlEnumValue("ActiveByNotam")
    ACTIVE_BY_NOTAM("ActiveByNotam");
    private final String value;

    TimeOfUse(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimeOfUse fromValue(String v) {
        for (TimeOfUse c: TimeOfUse.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
