
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for H24Indicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="H24Indicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Continuous"/&gt;
 *     &lt;enumeration value="NotContinuous"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "H24Indicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum H24Indicator {


    /**
     * Frequency is continually available,
     * 
     */
    @XmlEnumValue("Continuous")
    CONTINUOUS("Continuous"),

    /**
     * Frequency is not continually available,
     * 
     */
    @XmlEnumValue("NotContinuous")
    NOT_CONTINUOUS("NotContinuous"),

    /**
     * Unknown
     * 
     */
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    H24Indicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static H24Indicator fromValue(String v) {
        for (H24Indicator c: H24Indicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
