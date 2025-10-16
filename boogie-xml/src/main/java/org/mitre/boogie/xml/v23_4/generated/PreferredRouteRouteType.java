
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PreferredRouteRouteType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PreferredRouteRouteType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NorthAtlanticTrafficCommon"/&gt;
 *     &lt;enumeration value="Preferential"/&gt;
 *     &lt;enumeration value="PacificOceanTransition"/&gt;
 *     &lt;enumeration value="TacanAustralia"/&gt;
 *     &lt;enumeration value="NorthAtlanticTrafficNonCommon"/&gt;
 *     &lt;enumeration value="PreferredPreferentialOverfly"/&gt;
 *     &lt;enumeration value="Preferred"/&gt;
 *     &lt;enumeration value="Tos"/&gt;
 *     &lt;enumeration value="Tec"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PreferredRouteRouteType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PreferredRouteRouteType {


    /**
     * North American Route for North Atlantic Traffic - Common Portion
     * 
     */
    @XmlEnumValue("NorthAtlanticTrafficCommon")
    NORTH_ATLANTIC_TRAFFIC_COMMON("NorthAtlanticTrafficCommon"),

    /**
     * Preferential Route
     * 
     */
    @XmlEnumValue("Preferential")
    PREFERENTIAL("Preferential"),

    /**
     * Pacific Oceanic Transition Routes (PACOTS)
     * 
     */
    @XmlEnumValue("PacificOceanTransition")
    PACIFIC_OCEAN_TRANSITION("PacificOceanTransition"),

    /**
     * TACAN Route - Australia
     * 
     */
    @XmlEnumValue("TacanAustralia")
    TACAN_AUSTRALIA("TacanAustralia"),

    /**
     * North American Routes for North Atlantic Traffic - Non-common Portion
     * 
     */
    @XmlEnumValue("NorthAtlanticTrafficNonCommon")
    NORTH_ATLANTIC_TRAFFIC_NON_COMMON("NorthAtlanticTrafficNonCommon"),

    /**
     * Preferred/Preferential Overfly Routes
     * 
     */
    @XmlEnumValue("PreferredPreferentialOverfly")
    PREFERRED_PREFERENTIAL_OVERFLY("PreferredPreferentialOverfly"),

    /**
     * Preferred Routes
     * 
     */
    @XmlEnumValue("Preferred")
    PREFERRED("Preferred"),

    /**
     * Traffic Orientation System Routes (TOS)
     * 
     */
    @XmlEnumValue("Tos")
    TOS("Tos"),

    /**
     * Tower Enroute Control Routes (TEC)
     * 
     */
    @XmlEnumValue("Tec")
    TEC("Tec");
    private final String value;

    PreferredRouteRouteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PreferredRouteRouteType fromValue(String v) {
        for (PreferredRouteRouteType c: PreferredRouteRouteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
