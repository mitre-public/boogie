
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BoundaryVia.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="BoundaryVia"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Circle"/&gt;
 *     &lt;enumeration value="GreatCircle"/&gt;
 *     &lt;enumeration value="RhumbLine"/&gt;
 *     &lt;enumeration value="CounterClockwiseArc"/&gt;
 *     &lt;enumeration value="ClockwiseArc"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BoundaryVia", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum BoundaryVia {


    /**
     * Circle
     * 
     */
    @XmlEnumValue("Circle")
    CIRCLE("Circle"),

    /**
     * Great Circle
     * 
     */
    @XmlEnumValue("GreatCircle")
    GREAT_CIRCLE("GreatCircle"),

    /**
     * Rhumb Line
     * 
     */
    @XmlEnumValue("RhumbLine")
    RHUMB_LINE("RhumbLine"),

    /**
     * Counter Clockwise ARC
     * 
     */
    @XmlEnumValue("CounterClockwiseArc")
    COUNTER_CLOCKWISE_ARC("CounterClockwiseArc"),

    /**
     * Clockwise ARC
     * 
     */
    @XmlEnumValue("ClockwiseArc")
    CLOCKWISE_ARC("ClockwiseArc");
    private final String value;

    BoundaryVia(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BoundaryVia fromValue(String v) {
        for (BoundaryVia c: BoundaryVia.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
