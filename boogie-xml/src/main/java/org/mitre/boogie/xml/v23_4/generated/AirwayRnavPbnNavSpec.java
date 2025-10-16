
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirwayRnavPbnNavSpec.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirwayRnavPbnNavSpec"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Rnav10"/&gt;
 *     &lt;enumeration value="Rnav5"/&gt;
 *     &lt;enumeration value="Rnav4"/&gt;
 *     &lt;enumeration value="Rnav2"/&gt;
 *     &lt;enumeration value="Rnav1"/&gt;
 *     &lt;enumeration value="BRnav"/&gt;
 *     &lt;enumeration value="PRnav"/&gt;
 *     &lt;enumeration value="PbnUnspecified"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirwayRnavPbnNavSpec", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AirwayRnavPbnNavSpec {

    @XmlEnumValue("Rnav10")
    RNAV_10("Rnav10"),
    @XmlEnumValue("Rnav5")
    RNAV_5("Rnav5"),
    @XmlEnumValue("Rnav4")
    RNAV_4("Rnav4"),
    @XmlEnumValue("Rnav2")
    RNAV_2("Rnav2"),
    @XmlEnumValue("Rnav1")
    RNAV_1("Rnav1"),
    @XmlEnumValue("BRnav")
    B_RNAV("BRnav"),
    @XmlEnumValue("PRnav")
    P_RNAV("PRnav"),
    @XmlEnumValue("PbnUnspecified")
    PBN_UNSPECIFIED("PbnUnspecified");
    private final String value;

    AirwayRnavPbnNavSpec(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AirwayRnavPbnNavSpec fromValue(String v) {
        for (AirwayRnavPbnNavSpec c: AirwayRnavPbnNavSpec.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
