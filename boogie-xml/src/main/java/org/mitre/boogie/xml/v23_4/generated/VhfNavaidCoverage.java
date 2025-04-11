
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VhfNavaidCoverage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="VhfNavaidCoverage"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Terminal"/&gt;
 *     &lt;enumeration value="Low"/&gt;
 *     &lt;enumeration value="High"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *     &lt;enumeration value="IlsTacan"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VhfNavaidCoverage", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum VhfNavaidCoverage {


    /**
     * Terminal. Generally usable within 25NM of the facility and below 12000 feet
     * 
     */
    @XmlEnumValue("Terminal")
    TERMINAL("Terminal"),

    /**
     * Low Altitude. Generally usable within 40NM of the facility and up to 18000 feet.
     * 
     */
    @XmlEnumValue("Low")
    LOW("Low"),

    /**
     * High Altitude. Generally usable Within 130NM of the facility and up 60000 feet.
     * 
     */
    @XmlEnumValue("High")
    HIGH("High"),

    /**
     * Undefined. Coverage not defined by government source.
     * 
     */
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined"),

    /**
     * ILS/TACAN. Full TACAN facility frequency-paired and operating with the same identifier as an ILS Localizer. Coverage is Terminal.
     * 
     */
    @XmlEnumValue("IlsTacan")
    ILS_TACAN("IlsTacan");
    private final String value;

    VhfNavaidCoverage(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VhfNavaidCoverage fromValue(String v) {
        for (VhfNavaidCoverage c: VhfNavaidCoverage.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
