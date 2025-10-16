
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RunwaySuffix.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RunwaySuffix"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="WaterSealaneOrWaterway"/&gt;
 *     &lt;enumeration value="GliderRunway"/&gt;
 *     &lt;enumeration value="UltralightRunway"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RunwaySuffix", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RunwaySuffix {

    @XmlEnumValue("WaterSealaneOrWaterway")
    WATER_SEALANE_OR_WATERWAY("WaterSealaneOrWaterway"),
    @XmlEnumValue("GliderRunway")
    GLIDER_RUNWAY("GliderRunway"),
    @XmlEnumValue("UltralightRunway")
    ULTRALIGHT_RUNWAY("UltralightRunway");
    private final String value;

    RunwaySuffix(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RunwaySuffix fromValue(String v) {
        for (RunwaySuffix c: RunwaySuffix.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
