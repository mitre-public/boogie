
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AircraftOrAircraftGroup.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AircraftOrAircraftGroup"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="All"/&gt;
 *     &lt;enumeration value="AllSpeed250OrLess"/&gt;
 *     &lt;enumeration value="NonJetAndTurboProp"/&gt;
 *     &lt;enumeration value="MultiEnginePropsOnly"/&gt;
 *     &lt;enumeration value="JetsTurbopropsSpecialCruise190OrGreater"/&gt;
 *     &lt;enumeration value="Helicopter"/&gt;
 *     &lt;enumeration value="JetPower"/&gt;
 *     &lt;enumeration value="TurbopropSpecialCruise190orGreater"/&gt;
 *     &lt;enumeration value="NonJetNonTurboprop"/&gt;
 *     &lt;enumeration value="NonJetCruise190OrGreater"/&gt;
 *     &lt;enumeration value="NonJetCruise190OrLess"/&gt;
 *     &lt;enumeration value="AircraftAsDefinedInNotes"/&gt;
 *     &lt;enumeration value="SingleEngine"/&gt;
 *     &lt;enumeration value="TwinEngine"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AircraftOrAircraftGroup", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AircraftOrAircraftGroup {


    /**
     * All Aircraft
     * 
     */
    @XmlEnumValue("All")
    ALL("All"),

    /**
     * All Aircraft, Cruise speed 250 kts or less
     * 
     */
    @XmlEnumValue("AllSpeed250OrLess")
    ALL_SPEED_250_OR_LESS("AllSpeed250OrLess"),

    /**
     * Non-Jet and Turbo Prop
     * 
     */
    @XmlEnumValue("NonJetAndTurboProp")
    NON_JET_AND_TURBO_PROP("NonJetAndTurboProp"),

    /**
     * Multi-Engine Props Only
     * 
     */
    @XmlEnumValue("MultiEnginePropsOnly")
    MULTI_ENGINE_PROPS_ONLY("MultiEnginePropsOnly"),

    /**
     * Jets and Turbo Props/Special, Cruise Speed 190 kts or greater
     * 
     */
    @XmlEnumValue("JetsTurbopropsSpecialCruise190OrGreater")
    JETS_TURBOPROPS_SPECIAL_CRUISE_190_OR_GREATER("JetsTurbopropsSpecialCruise190OrGreater"),

    /**
     * Helicopter Only
     * 
     */
    @XmlEnumValue("Helicopter")
    HELICOPTER("Helicopter"),

    /**
     * Jet Power
     * 
     */
    @XmlEnumValue("JetPower")
    JET_POWER("JetPower"),

    /**
     * Turbo-Prop/Special, Cruise Speed 190 kts or greater
     * 
     */
    @XmlEnumValue("TurbopropSpecialCruise190orGreater")
    TURBOPROP_SPECIAL_CRUISE_190_OR_GREATER("TurbopropSpecialCruise190orGreater"),

    /**
     * Non-Jet, Non-Turbo Prop
     * 
     */
    @XmlEnumValue("NonJetNonTurboprop")
    NON_JET_NON_TURBOPROP("NonJetNonTurboprop"),

    /**
     * Non-Jet, Cruise speed 190 kts or greater
     * 
     */
    @XmlEnumValue("NonJetCruise190OrGreater")
    NON_JET_CRUISE_190_OR_GREATER("NonJetCruise190OrGreater"),

    /**
     * Non-Jet, Cruise speed 189 kts or less
     * 
     */
    @XmlEnumValue("NonJetCruise190OrLess")
    NON_JET_CRUISE_190_OR_LESS("NonJetCruise190OrLess"),

    /**
     * Aircraft as defined in a Notes Continuation Record
     * 
     */
    @XmlEnumValue("AircraftAsDefinedInNotes")
    AIRCRAFT_AS_DEFINED_IN_NOTES("AircraftAsDefinedInNotes"),

    /**
     * Single Engine
     * 
     */
    @XmlEnumValue("SingleEngine")
    SINGLE_ENGINE("SingleEngine"),

    /**
     * Twin Engine
     * 
     */
    @XmlEnumValue("TwinEngine")
    TWIN_ENGINE("TwinEngine");
    private final String value;

    AircraftOrAircraftGroup(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AircraftOrAircraftGroup fromValue(String v) {
        for (AircraftOrAircraftGroup c: AircraftOrAircraftGroup.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
