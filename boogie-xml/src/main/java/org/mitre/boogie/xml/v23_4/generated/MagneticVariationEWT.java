
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MagneticVariationEWT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MagneticVariationEWT"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="East"/&gt;
 *     &lt;enumeration value="West"/&gt;
 *     &lt;enumeration value="True"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MagneticVariationEWT", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MagneticVariationEWT {


    /**
     * Magnetic variation is East of TRUE North
     * 
     */
    @XmlEnumValue("East")
    EAST("East"),

    /**
     * Magnetic variation is West of TRUE North
     * 
     */
    @XmlEnumValue("West")
    WEST("West"),

    /**
     * The element defined in the current record is provided TRUE.
     * 
     */
    @XmlEnumValue("True")
    TRUE("True");
    private final String value;

    MagneticVariationEWT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MagneticVariationEWT fromValue(String v) {
        for (MagneticVariationEWT c: MagneticVariationEWT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
