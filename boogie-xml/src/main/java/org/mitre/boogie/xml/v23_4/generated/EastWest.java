
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EastWest.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="EastWest"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="East"/&gt;
 *     &lt;enumeration value="West"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EastWest", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum EastWest {


    /**
     * East
     * 
     */
    @XmlEnumValue("East")
    EAST("East"),

    /**
     * West
     * 
     */
    @XmlEnumValue("West")
    WEST("West");
    private final String value;

    EastWest(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EastWest fromValue(String v) {
        for (EastWest c: EastWest.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
