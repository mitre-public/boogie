
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RestrictiveAirspaceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RestrictiveAirspaceType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Alert"/&gt;
 *     &lt;enumeration value="Caution"/&gt;
 *     &lt;enumeration value="Danger"/&gt;
 *     &lt;enumeration value="LongTermTFR"/&gt;
 *     &lt;enumeration value="MilitaryOps"/&gt;
 *     &lt;enumeration value="NationalSecurity"/&gt;
 *     &lt;enumeration value="Prohibited"/&gt;
 *     &lt;enumeration value="Restricted"/&gt;
 *     &lt;enumeration value="Training"/&gt;
 *     &lt;enumeration value="Warning"/&gt;
 *     &lt;enumeration value="Unspecified"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RestrictiveAirspaceType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RestrictiveAirspaceType {


    /**
     * Alert
     * 
     */
    @XmlEnumValue("Alert")
    ALERT("Alert"),

    /**
     * Caution
     * 
     */
    @XmlEnumValue("Caution")
    CAUTION("Caution"),

    /**
     * Danger
     * 
     */
    @XmlEnumValue("Danger")
    DANGER("Danger"),

    /**
     * Long-term TFR
     * 
     */
    @XmlEnumValue("LongTermTFR")
    LONG_TERM_TFR("LongTermTFR"),

    /**
     * Military Operations Area
     * 
     */
    @XmlEnumValue("MilitaryOps")
    MILITARY_OPS("MilitaryOps"),

    /**
     * National Security Area
     * 
     */
    @XmlEnumValue("NationalSecurity")
    NATIONAL_SECURITY("NationalSecurity"),

    /**
     * Prohibited
     * 
     */
    @XmlEnumValue("Prohibited")
    PROHIBITED("Prohibited"),

    /**
     * Restricted
     * 
     */
    @XmlEnumValue("Restricted")
    RESTRICTED("Restricted"),

    /**
     * Training
     * 
     */
    @XmlEnumValue("Training")
    TRAINING("Training"),

    /**
     * Warning
     * 
     */
    @XmlEnumValue("Warning")
    WARNING("Warning"),

    /**
     * Unspecified or Unknown
     * 
     */
    @XmlEnumValue("Unspecified")
    UNSPECIFIED("Unspecified");
    private final String value;

    RestrictiveAirspaceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RestrictiveAirspaceType fromValue(String v) {
        for (RestrictiveAirspaceType c: RestrictiveAirspaceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
