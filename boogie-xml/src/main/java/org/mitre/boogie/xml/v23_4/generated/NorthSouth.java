
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NorthSouth.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NorthSouth"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="North"/&gt;
 *     &lt;enumeration value="South"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NorthSouth", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NorthSouth {


    /**
     * North
     * 
     */
    @XmlEnumValue("North")
    NORTH("North"),

    /**
     * South
     * 
     */
    @XmlEnumValue("South")
    SOUTH("South");
    private final String value;

    NorthSouth(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NorthSouth fromValue(String v) {
        for (NorthSouth c: NorthSouth.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
