
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EnrouteAirwayDirectionalRestriction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="EnrouteAirwayDirectionalRestriction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OneWayForward"/&gt;
 *     &lt;enumeration value="OneWayBackward"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EnrouteAirwayDirectionalRestriction", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum EnrouteAirwayDirectionalRestriction {


    /**
     * One-way in direction route is coded (Forward).
     * 
     */
    @XmlEnumValue("OneWayForward")
    ONE_WAY_FORWARD("OneWayForward"),

    /**
     * One-way in opposite direction route is coded (backward).
     * 
     */
    @XmlEnumValue("OneWayBackward")
    ONE_WAY_BACKWARD("OneWayBackward");
    private final String value;

    EnrouteAirwayDirectionalRestriction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnrouteAirwayDirectionalRestriction fromValue(String v) {
        for (EnrouteAirwayDirectionalRestriction c: EnrouteAirwayDirectionalRestriction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
