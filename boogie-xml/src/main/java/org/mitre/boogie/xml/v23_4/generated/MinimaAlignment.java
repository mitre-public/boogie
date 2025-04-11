
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MinimaAlignment.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MinimaAlignment"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CircleToLand"/&gt;
 *     &lt;enumeration value="Helicopter"/&gt;
 *     &lt;enumeration value="SideStep"/&gt;
 *     &lt;enumeration value="StraightIn"/&gt;
 *     &lt;enumeration value="Visual"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MinimaAlignment", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MinimaAlignment {

    @XmlEnumValue("CircleToLand")
    CIRCLE_TO_LAND("CircleToLand"),
    @XmlEnumValue("Helicopter")
    HELICOPTER("Helicopter"),
    @XmlEnumValue("SideStep")
    SIDE_STEP("SideStep"),
    @XmlEnumValue("StraightIn")
    STRAIGHT_IN("StraightIn"),
    @XmlEnumValue("Visual")
    VISUAL("Visual");
    private final String value;

    MinimaAlignment(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MinimaAlignment fromValue(String v) {
        for (MinimaAlignment c: MinimaAlignment.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
