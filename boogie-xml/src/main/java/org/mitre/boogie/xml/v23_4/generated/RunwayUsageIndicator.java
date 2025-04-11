
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RunwayUsageIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RunwayUsageIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LandingOnly"/&gt;
 *     &lt;enumeration value="TakeoffOnly"/&gt;
 *     &lt;enumeration value="TakeoffAndLanding"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RunwayUsageIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RunwayUsageIndicator {

    @XmlEnumValue("LandingOnly")
    LANDING_ONLY("LandingOnly"),
    @XmlEnumValue("TakeoffOnly")
    TAKEOFF_ONLY("TakeoffOnly"),
    @XmlEnumValue("TakeoffAndLanding")
    TAKEOFF_AND_LANDING("TakeoffAndLanding");
    private final String value;

    RunwayUsageIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RunwayUsageIndicator fromValue(String v) {
        for (RunwayUsageIndicator c: RunwayUsageIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
