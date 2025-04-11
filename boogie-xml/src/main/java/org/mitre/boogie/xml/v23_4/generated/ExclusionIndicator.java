
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExclusionIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ExclusionIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AllBoth"/&gt;
 *     &lt;enumeration value="AllOpposite"/&gt;
 *     &lt;enumeration value="AllPerAirway"/&gt;
 *     &lt;enumeration value="NotAll"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ExclusionIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ExclusionIndicator {


    /**
     * All altitudes in both directions of flight are restricted. This effectively closes the airway in both direction of flight.
     * 
     */
    @XmlEnumValue("AllBoth")
    ALL_BOTH("AllBoth"),

    /**
     * All altitudes in the opposite direction in which the Enroute Airway is coded are restricted. This effectively closes the airway in one direction of flight i.e., the opposite direction from that in which the airway is coded.
     * 
     */
    @XmlEnumValue("AllOpposite")
    ALL_OPPOSITE("AllOpposite"),

    /**
     * All altitudes in the direction in which the Enroute Airway is coded are restricted. This effectively closes the airway in one direction of flight i.e., the direction in which the airway is coded.
     * 
     */
    @XmlEnumValue("AllPerAirway")
    ALL_PER_AIRWAY("AllPerAirway"),

    /**
     * The restriction is not an all altitude restriction.
     * 
     */
    @XmlEnumValue("NotAll")
    NOT_ALL("NotAll");
    private final String value;

    ExclusionIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExclusionIndicator fromValue(String v) {
        for (ExclusionIndicator c: ExclusionIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
