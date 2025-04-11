
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EnrouteAirwayRouteType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="EnrouteAirwayRouteType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Airline"/&gt;
 *     &lt;enumeration value="Control"/&gt;
 *     &lt;enumeration value="Direct"/&gt;
 *     &lt;enumeration value="Helicopter"/&gt;
 *     &lt;enumeration value="OfficiallyDesignatedExceptRnavHelicopter"/&gt;
 *     &lt;enumeration value="RnavRNP"/&gt;
 *     &lt;enumeration value="Undesignated"/&gt;
 *     &lt;enumeration value="Tacan"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EnrouteAirwayRouteType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum EnrouteAirwayRouteType {


    /**
     * Airline Airway (Tailored Data)
     * 
     */
    @XmlEnumValue("Airline")
    AIRLINE("Airline"),

    /**
     * Control
     * 
     */
    @XmlEnumValue("Control")
    CONTROL("Control"),

    /**
     * Direct Route
     * 
     */
    @XmlEnumValue("Direct")
    DIRECT("Direct"),

    /**
     * Helicopter Airways
     * 
     */
    @XmlEnumValue("Helicopter")
    HELICOPTER("Helicopter"),

    /**
     * Officially Designated Airways, except RNAV, RNP, or Helicopter Airways
     * 
     */
    @XmlEnumValue("OfficiallyDesignatedExceptRnavHelicopter")
    OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER("OfficiallyDesignatedExceptRnavHelicopter"),

    /**
     * RNAV or RNP Airways (ICAO PBN Nav Spec)
     * 
     */
    @XmlEnumValue("RnavRNP")
    RNAV_RNP("RnavRNP"),

    /**
     * Undesignated ATS Route
     * 
     */
    @XmlEnumValue("Undesignated")
    UNDESIGNATED("Undesignated"),

    /**
     * TACAN Airway
     * 
     */
    @XmlEnumValue("Tacan")
    TACAN("Tacan");
    private final String value;

    EnrouteAirwayRouteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnrouteAirwayRouteType fromValue(String v) {
        for (EnrouteAirwayRouteType c: EnrouteAirwayRouteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
