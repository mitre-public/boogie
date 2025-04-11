
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LegTypeCodeSC.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LegTypeCodeSC"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PointToPoint"/&gt;
 *     &lt;enumeration value="Curved"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LegTypeCodeSC", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum LegTypeCodeSC {


    /**
     * Straight Point to Point
     * 
     */
    @XmlEnumValue("PointToPoint")
    POINT_TO_POINT("PointToPoint"),

    /**
     * Curved line Flight Track
     * 
     */
    @XmlEnumValue("Curved")
    CURVED("Curved");
    private final String value;

    LegTypeCodeSC(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LegTypeCodeSC fromValue(String v) {
        for (LegTypeCodeSC c: LegTypeCodeSC.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
