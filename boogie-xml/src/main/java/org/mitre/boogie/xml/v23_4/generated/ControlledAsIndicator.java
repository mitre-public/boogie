
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ControlledAsIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ControlledAsIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="WithinOrBelowClassC"/&gt;
 *     &lt;enumeration value="WithinOrBelowCta"/&gt;
 *     &lt;enumeration value="WithinOrBelowTmsTca"/&gt;
 *     &lt;enumeration value="WithinOrBelowRadarZone"/&gt;
 *     &lt;enumeration value="WithinOrBelowClassB"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ControlledAsIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ControlledAsIndicator {


    /**
     * The Airport is within or below the lateral limits of Class C Airspace.
     * 
     */
    @XmlEnumValue("WithinOrBelowClassC")
    WITHIN_OR_BELOW_CLASS_C("WithinOrBelowClassC"),

    /**
     * The Airport is within or below the lateral limits of a CTA.
     * 
     */
    @XmlEnumValue("WithinOrBelowCta")
    WITHIN_OR_BELOW_CTA("WithinOrBelowCta"),

    /**
     * The Airport is within or below the lateral limits of a TMA or TCA.
     * 
     */
    @XmlEnumValue("WithinOrBelowTmsTca")
    WITHIN_OR_BELOW_TMS_TCA("WithinOrBelowTmsTca"),

    /**
     * The Airport is within or below the lateral limits Radar Zone
     * 
     */
    @XmlEnumValue("WithinOrBelowRadarZone")
    WITHIN_OR_BELOW_RADAR_ZONE("WithinOrBelowRadarZone"),

    /**
     * The Airport is within or below the lateral limits of Class B Airspace.
     * 
     */
    @XmlEnumValue("WithinOrBelowClassB")
    WITHIN_OR_BELOW_CLASS_B("WithinOrBelowClassB");
    private final String value;

    ControlledAsIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ControlledAsIndicator fromValue(String v) {
        for (ControlledAsIndicator c: ControlledAsIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
