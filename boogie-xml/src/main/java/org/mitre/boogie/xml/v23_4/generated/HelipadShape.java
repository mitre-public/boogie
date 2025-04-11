
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HelipadShape.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="HelipadShape"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Circle"/&gt;
 *     &lt;enumeration value="SquareOrRectangle"/&gt;
 *     &lt;enumeration value="Runway"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HelipadShape", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum HelipadShape {

    @XmlEnumValue("Circle")
    CIRCLE("Circle"),
    @XmlEnumValue("SquareOrRectangle")
    SQUARE_OR_RECTANGLE("SquareOrRectangle"),
    @XmlEnumValue("Runway")
    RUNWAY("Runway"),
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined");
    private final String value;

    HelipadShape(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HelipadShape fromValue(String v) {
        for (HelipadShape c: HelipadShape.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
