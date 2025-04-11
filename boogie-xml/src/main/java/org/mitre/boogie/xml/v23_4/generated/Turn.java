
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Turn.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Turn"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Left"/&gt;
 *     &lt;enumeration value="Right"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Turn", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum Turn {


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
    RIGHT("Right");
    private final String value;

    Turn(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Turn fromValue(String v) {
        for (Turn c: Turn.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
