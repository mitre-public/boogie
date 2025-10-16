
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActivityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ActivityType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Parachute"/&gt;
 *     &lt;enumeration value="Glider"/&gt;
 *     &lt;enumeration value="HangGlider"/&gt;
 *     &lt;enumeration value="UltraLight"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ActivityType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ActivityType {


    /**
     * Parachute Jump Area
     * 
     */
    @XmlEnumValue("Parachute")
    PARACHUTE("Parachute"),

    /**
     * Glider Operations
     * 
     */
    @XmlEnumValue("Glider")
    GLIDER("Glider"),

    /**
     * Hang Glider Activities
     * 
     */
    @XmlEnumValue("HangGlider")
    HANG_GLIDER("HangGlider"),

    /**
     * Ultra Light Activities
     * 
     */
    @XmlEnumValue("UltraLight")
    ULTRA_LIGHT("UltraLight");
    private final String value;

    ActivityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActivityType fromValue(String v) {
        for (ActivityType c: ActivityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
