
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RnavPbnNavSpec.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RnavPbnNavSpec"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Rnav5"/&gt;
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
@XmlType(name = "RnavPbnNavSpec", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RnavPbnNavSpec {

    @XmlEnumValue("Rnav5")
    RNAV_5("Rnav5"),
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

    RnavPbnNavSpec(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RnavPbnNavSpec fromValue(String v) {
        for (RnavPbnNavSpec c: RnavPbnNavSpec.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
