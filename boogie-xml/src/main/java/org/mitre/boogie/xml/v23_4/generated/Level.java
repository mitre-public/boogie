
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Level.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Level"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AllAlt"/&gt;
 *     &lt;enumeration value="HighAlt"/&gt;
 *     &lt;enumeration value="LowAlt"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Level", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum Level {


    /**
     * All Altitudes
     * 
     */
    @XmlEnumValue("AllAlt")
    ALL_ALT("AllAlt"),

    /**
     * High Level Airways/Altitudes
     * 
     */
    @XmlEnumValue("HighAlt")
    HIGH_ALT("HighAlt"),

    /**
     * Low Level Airways/Altitudes
     * 
     */
    @XmlEnumValue("LowAlt")
    LOW_ALT("LowAlt");
    private final String value;

    Level(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Level fromValue(String v) {
        for (Level c: Level.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
