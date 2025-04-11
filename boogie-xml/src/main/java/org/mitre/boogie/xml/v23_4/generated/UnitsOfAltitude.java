
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnitsOfAltitude.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UnitsOfAltitude"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="HundredsFeet"/&gt;
 *     &lt;enumeration value="MetricFlightLevel"/&gt;
 *     &lt;enumeration value="FeetFlightLevel"/&gt;
 *     &lt;enumeration value="TensOfMeters"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UnitsOfAltitude", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum UnitsOfAltitude {


    /**
     * Restriction Altitudes are expressed in hundreds of feet
     * 
     */
    @XmlEnumValue("HundredsFeet")
    HUNDREDS_FEET("HundredsFeet"),

    /**
     * Restriction Altitudes are expressed in metric Flight Levels
     * 
     */
    @XmlEnumValue("MetricFlightLevel")
    METRIC_FLIGHT_LEVEL("MetricFlightLevel"),

    /**
     * Restriction Altitudes are expressed in feet Flight Levels
     * 
     */
    @XmlEnumValue("FeetFlightLevel")
    FEET_FLIGHT_LEVEL("FeetFlightLevel"),

    /**
     * Restriction Altitudes are expressed in tens of meters
     * 
     */
    @XmlEnumValue("TensOfMeters")
    TENS_OF_METERS("TensOfMeters");
    private final String value;

    UnitsOfAltitude(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UnitsOfAltitude fromValue(String v) {
        for (UnitsOfAltitude c: UnitsOfAltitude.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
