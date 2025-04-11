
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NavaidLimitationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NavaidLimitationCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Coverage"/&gt;
 *     &lt;enumeration value="Fluctuations"/&gt;
 *     &lt;enumeration value="Roughness"/&gt;
 *     &lt;enumeration value="Unreliable"/&gt;
 *     &lt;enumeration value="Restricted"/&gt;
 *     &lt;enumeration value="Unusable"/&gt;
 *     &lt;enumeration value="OutOfTolerance"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NavaidLimitationCode", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NavaidLimitationCode {


    /**
     * Coverage, the limitations are expressed as maximum reception reliability
     * 
     */
    @XmlEnumValue("Coverage")
    COVERAGE("Coverage"),

    /**
     * Fluctuations, radial(s) are affected by course fluctuations.
     * 
     */
    @XmlEnumValue("Fluctuations")
    FLUCTUATIONS("Fluctuations"),

    /**
     * Roughness, signal roughness experienced in the sector(s) defined.
     * 
     */
    @XmlEnumValue("Roughness")
    ROUGHNESS("Roughness"),

    /**
     * Unreliable in the sector(s), at the altitude(s), at the distance(s) defined.
     * 
     */
    @XmlEnumValue("Unreliable")
    UNRELIABLE("Unreliable"),

    /**
     * Restricted in the sector(s), at the altitude(s), at the distance(s) defined
     * 
     */
    @XmlEnumValue("Restricted")
    RESTRICTED("Restricted"),

    /**
     * Unusable in the sector(s), at the altitude(s), at the distance(s) defined.
     * 
     */
    @XmlEnumValue("Unusable")
    UNUSABLE("Unusable"),

    /**
     * Out of Tolerance in the sector(s), at the altitude(s), at the distance(s) defined.
     * 
     */
    @XmlEnumValue("OutOfTolerance")
    OUT_OF_TOLERANCE("OutOfTolerance");
    private final String value;

    NavaidLimitationCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NavaidLimitationCode fromValue(String v) {
        for (NavaidLimitationCode c: NavaidLimitationCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
