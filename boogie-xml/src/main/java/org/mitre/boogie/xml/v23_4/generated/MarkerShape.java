
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MarkerShape.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MarkerShape"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Bone"/&gt;
 *     &lt;enumeration value="Elliptical"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MarkerShape", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MarkerShape {


    /**
     * Bone Shape
     * 
     */
    @XmlEnumValue("Bone")
    BONE("Bone"),

    /**
     * Elliptical Shape
     * 
     */
    @XmlEnumValue("Elliptical")
    ELLIPTICAL("Elliptical");
    private final String value;

    MarkerShape(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MarkerShape fromValue(String v) {
        for (MarkerShape c: MarkerShape.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
