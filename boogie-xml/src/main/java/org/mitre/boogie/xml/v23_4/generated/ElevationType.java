
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ElevationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ElevationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LandingThreshold"/&gt;
 *     &lt;enumeration value="Port"/&gt;
 *     &lt;enumeration value="RunwayEnd"/&gt;
 *     &lt;enumeration value="TouchdownZone"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ElevationType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ElevationType {


    /**
     * Landing threshold elevation
     * 
     */
    @XmlEnumValue("LandingThreshold")
    LANDING_THRESHOLD("LandingThreshold"),

    /**
     * Airport/Heliport elevation
     * 
     */
    @XmlEnumValue("Port")
    PORT("Port"),

    /**
     * Runway end elevation and the landing threshold is a displaced threshold
     * 
     */
    @XmlEnumValue("RunwayEnd")
    RUNWAY_END("RunwayEnd"),

    /**
     * Touch-down zone elevation
     * 
     */
    @XmlEnumValue("TouchdownZone")
    TOUCHDOWN_ZONE("TouchdownZone");
    private final String value;

    ElevationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ElevationType fromValue(String v) {
        for (ElevationType c: ElevationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
