
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RnpPbnNavSpec.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RnpPbnNavSpec"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Rnp2"/&gt;
 *     &lt;enumeration value="Rnp1"/&gt;
 *     &lt;enumeration value="RnpAr"/&gt;
 *     &lt;enumeration value="AdvRnp"/&gt;
 *     &lt;enumeration value="Rnp0_3"/&gt;
 *     &lt;enumeration value="PbnUnspecified"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RnpPbnNavSpec", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RnpPbnNavSpec {

    @XmlEnumValue("Rnp2")
    RNP_2("Rnp2"),
    @XmlEnumValue("Rnp1")
    RNP_1("Rnp1"),
    @XmlEnumValue("RnpAr")
    RNP_AR("RnpAr"),
    @XmlEnumValue("AdvRnp")
    ADV_RNP("AdvRnp"),
    @XmlEnumValue("Rnp0_3")
    RNP_0_3("Rnp0_3"),
    @XmlEnumValue("PbnUnspecified")
    PBN_UNSPECIFIED("PbnUnspecified");
    private final String value;

    RnpPbnNavSpec(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RnpPbnNavSpec fromValue(String v) {
        for (RnpPbnNavSpec c: RnpPbnNavSpec.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
