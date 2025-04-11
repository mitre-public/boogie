
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TurnDirection.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TurnDirection"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Left"/&gt;
 *     &lt;enumeration value="Right"/&gt;
 *     &lt;enumeration value="Either"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TurnDirection", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TurnDirection {


    /**
     * Left
     * 
     */
    @XmlEnumValue("Left")
    LEFT("Left"),

    /**
     * Right
     * 
     */
    @XmlEnumValue("Right")
    RIGHT("Right"),

    /**
     * Either Left or Right
     * 
     */
    @XmlEnumValue("Either")
    EITHER("Either");
    private final String value;

    TurnDirection(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TurnDirection fromValue(String v) {
        for (TurnDirection c: TurnDirection.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
