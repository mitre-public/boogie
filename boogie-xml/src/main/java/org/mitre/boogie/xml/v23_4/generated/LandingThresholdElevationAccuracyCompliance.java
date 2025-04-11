
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LandingThresholdElevationAccuracyCompliance.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LandingThresholdElevationAccuracyCompliance"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Compliant"/&gt;
 *     &lt;enumeration value="NotCompliant"/&gt;
 *     &lt;enumeration value="NotEvaluated"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LandingThresholdElevationAccuracyCompliance", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum LandingThresholdElevationAccuracyCompliance {

    @XmlEnumValue("Compliant")
    COMPLIANT("Compliant"),
    @XmlEnumValue("NotCompliant")
    NOT_COMPLIANT("NotCompliant"),
    @XmlEnumValue("NotEvaluated")
    NOT_EVALUATED("NotEvaluated");
    private final String value;

    LandingThresholdElevationAccuracyCompliance(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LandingThresholdElevationAccuracyCompliance fromValue(String v) {
        for (LandingThresholdElevationAccuracyCompliance c: LandingThresholdElevationAccuracyCompliance.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
