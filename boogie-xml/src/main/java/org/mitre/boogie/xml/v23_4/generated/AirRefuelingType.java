
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirRefuelingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirRefuelingType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="anchor"/&gt;
 *     &lt;enumeration value="track"/&gt;
 *     &lt;enumeration value="anchor or track"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirRefuelingType")
@XmlEnum
public enum AirRefuelingType {

    @XmlEnumValue("anchor")
    ANCHOR("anchor"),
    @XmlEnumValue("track")
    TRACK("track"),
    @XmlEnumValue("anchor or track")
    ANCHOR_OR_TRACK("anchor or track");
    private final String value;

    AirRefuelingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AirRefuelingType fromValue(String v) {
        for (AirRefuelingType c: AirRefuelingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
