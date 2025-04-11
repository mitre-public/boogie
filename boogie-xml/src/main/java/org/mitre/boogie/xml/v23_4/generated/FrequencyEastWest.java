
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FrequencyEastWest.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FrequencyEastWest"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="east"/&gt;
 *     &lt;enumeration value="west"/&gt;
 *     &lt;enumeration value="both east and west"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FrequencyEastWest")
@XmlEnum
public enum FrequencyEastWest {

    @XmlEnumValue("east")
    EAST("east"),
    @XmlEnumValue("west")
    WEST("west"),
    @XmlEnumValue("both east and west")
    BOTH_EAST_AND_WEST("both east and west");
    private final String value;

    FrequencyEastWest(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FrequencyEastWest fromValue(String v) {
        for (FrequencyEastWest c: FrequencyEastWest.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
