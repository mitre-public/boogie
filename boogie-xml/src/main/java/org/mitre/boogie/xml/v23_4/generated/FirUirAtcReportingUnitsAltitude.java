
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirUirAtcReportingUnitsAltitude.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FirUirAtcReportingUnitsAltitude"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NotSpecified"/&gt;
 *     &lt;enumeration value="FlightLevel"/&gt;
 *     &lt;enumeration value="Meters"/&gt;
 *     &lt;enumeration value="Feet"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FirUirAtcReportingUnitsAltitude", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FirUirAtcReportingUnitsAltitude {


    /**
     * Not specified
     * 
     */
    @XmlEnumValue("NotSpecified")
    NOT_SPECIFIED("NotSpecified"),

    /**
     * ALT in Flight Level
     * 
     */
    @XmlEnumValue("FlightLevel")
    FLIGHT_LEVEL("FlightLevel"),

    /**
     * ALT in Meters
     * 
     */
    @XmlEnumValue("Meters")
    METERS("Meters"),

    /**
     * TAS in Feet
     * 
     */
    @XmlEnumValue("Feet")
    FEET("Feet");
    private final String value;

    FirUirAtcReportingUnitsAltitude(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FirUirAtcReportingUnitsAltitude fromValue(String v) {
        for (FirUirAtcReportingUnitsAltitude c: FirUirAtcReportingUnitsAltitude.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
