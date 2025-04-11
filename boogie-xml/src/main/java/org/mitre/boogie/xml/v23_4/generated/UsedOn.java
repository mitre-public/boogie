
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UsedOn.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UsedOn"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AirportComms"/&gt;
 *     &lt;enumeration value="EnrouteComms"/&gt;
 *     &lt;enumeration value="HeliportComms"/&gt;
 *     &lt;enumeration value="AirportHeliportEnrouteComms"/&gt;
 *     &lt;enumeration value="AirportHeliportComms"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UsedOn", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum UsedOn {


    /**
     * Type is used on Airport Communication Records only
     * 
     */
    @XmlEnumValue("AirportComms")
    AIRPORT_COMMS("AirportComms"),

    /**
     * Type is used on Enroute Communication Records only
     * 
     */
    @XmlEnumValue("EnrouteComms")
    ENROUTE_COMMS("EnrouteComms"),

    /**
     * Type is used on Heliport Communication Records only
     * 
     */
    @XmlEnumValue("HeliportComms")
    HELIPORT_COMMS("HeliportComms"),

    /**
     * Type is used on Airport, Heliport and Enroute Communication Records
     * 
     */
    @XmlEnumValue("AirportHeliportEnrouteComms")
    AIRPORT_HELIPORT_ENROUTE_COMMS("AirportHeliportEnrouteComms"),

    /**
     * Type is used on Airport and Heliport Communication Records
     * 
     */
    @XmlEnumValue("AirportHeliportComms")
    AIRPORT_HELIPORT_COMMS("AirportHeliportComms");
    private final String value;

    UsedOn(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsedOn fromValue(String v) {
        for (UsedOn c: UsedOn.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
