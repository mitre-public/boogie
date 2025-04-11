
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TchValueIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TchValueIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TchOfElectricGlideSlope"/&gt;
 *     &lt;enumeration value="TchRnavToRunway"/&gt;
 *     &lt;enumeration value="TchVgsi"/&gt;
 *     &lt;enumeration value="TchDefaulted"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TchValueIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TchValueIndicator {


    /**
     * TCH provided in Runway Record is that of the electronic Glide Slope
     * 
     */
    @XmlEnumValue("TchOfElectricGlideSlope")
    TCH_OF_ELECTRIC_GLIDE_SLOPE("TchOfElectricGlideSlope"),

    /**
     * TCH provided in Runway Record is that of an RNAV procedure to the runway
     * 
     */
    @XmlEnumValue("TchRnavToRunway")
    TCH_RNAV_TO_RUNWAY("TchRnavToRunway"),

    /**
     * TCH provided in the Runway Record is that of the VGSI for the runway
     * 
     */
    @XmlEnumValue("TchVgsi")
    TCH_VGSI("TchVgsi"),

    /**
     * TCH provided in the Runway Record is the default value of 40 or 50 feet (See Section 5.67).
     * 
     */
    @XmlEnumValue("TchDefaulted")
    TCH_DEFAULTED("TchDefaulted");
    private final String value;

    TchValueIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TchValueIndicator fromValue(String v) {
        for (TchValueIndicator c: TchValueIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
