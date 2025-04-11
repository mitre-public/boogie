
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirwayRnpPbnNavSpec.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirwayRnpPbnNavSpec"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Rnp4"/&gt;
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
@XmlType(name = "AirwayRnpPbnNavSpec", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AirwayRnpPbnNavSpec {

    @XmlEnumValue("Rnp4")
    RNP_4("Rnp4"),
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

    AirwayRnpPbnNavSpec(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AirwayRnpPbnNavSpec fromValue(String v) {
        for (AirwayRnpPbnNavSpec c: AirwayRnpPbnNavSpec.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
