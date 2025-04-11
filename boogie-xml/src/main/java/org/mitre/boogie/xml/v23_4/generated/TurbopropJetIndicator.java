
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TurbopropJetIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TurbopropJetIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="All"/&gt;
 *     &lt;enumeration value="JetsAndTurboProps"/&gt;
 *     &lt;enumeration value="AllCruise250OrLess"/&gt;
 *     &lt;enumeration value="NonJetAndTurboProp"/&gt;
 *     &lt;enumeration value="MultiEngineProps"/&gt;
 *     &lt;enumeration value="Jets"/&gt;
 *     &lt;enumeration value="NonJetNonTurboprop"/&gt;
 *     &lt;enumeration value="TurboProp"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TurbopropJetIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TurbopropJetIndicator {


    /**
     * All Aircraft
     * 
     */
    @XmlEnumValue("All")
    ALL("All"),

    /**
     * Jets and Turbo Props
     * 
     */
    @XmlEnumValue("JetsAndTurboProps")
    JETS_AND_TURBO_PROPS("JetsAndTurboProps"),

    /**
     * All Aircraft, Cruise speed 250 kts or less
     * 
     */
    @XmlEnumValue("AllCruise250OrLess")
    ALL_CRUISE_250_OR_LESS("AllCruise250OrLess"),

    /**
     * Non-jet and Turbo Prop
     * 
     */
    @XmlEnumValue("NonJetAndTurboProp")
    NON_JET_AND_TURBO_PROP("NonJetAndTurboProp"),

    /**
     * Multi-Engine Props Only
     * 
     */
    @XmlEnumValue("MultiEngineProps")
    MULTI_ENGINE_PROPS("MultiEngineProps"),

    /**
     * Jets
     * 
     */
    @XmlEnumValue("Jets")
    JETS("Jets"),

    /**
     * Non-Jet, Non-Turbo Prop
     * 
     */
    @XmlEnumValue("NonJetNonTurboprop")
    NON_JET_NON_TURBOPROP("NonJetNonTurboprop"),

    /**
     * Turbo Props
     * 
     */
    @XmlEnumValue("TurboProp")
    TURBO_PROP("TurboProp");
    private final String value;

    TurbopropJetIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TurbopropJetIndicator fromValue(String v) {
        for (TurbopropJetIndicator c: TurbopropJetIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
