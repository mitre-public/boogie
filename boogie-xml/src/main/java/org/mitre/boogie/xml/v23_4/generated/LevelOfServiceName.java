
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LevelOfServiceName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LevelOfServiceName"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;maxLength value="10"/&gt;
 *     &lt;enumeration value="LPV"/&gt;
 *     &lt;enumeration value="LPV200"/&gt;
 *     &lt;enumeration value="LP"/&gt;
 *     &lt;enumeration value="LNAV"/&gt;
 *     &lt;enumeration value="LNAV/VNAV"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LevelOfServiceName", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum LevelOfServiceName {

    LPV("LPV"),
    @XmlEnumValue("LPV200")
    LPV_200("LPV200"),
    LP("LP"),
    LNAV("LNAV"),
    @XmlEnumValue("LNAV/VNAV")
    LNAV_VNAV("LNAV/VNAV");
    private final String value;

    LevelOfServiceName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LevelOfServiceName fromValue(String v) {
        for (LevelOfServiceName c: LevelOfServiceName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
