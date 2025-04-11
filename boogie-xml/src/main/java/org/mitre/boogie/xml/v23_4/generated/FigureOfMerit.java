
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FigureOfMerit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FigureOfMerit"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TerminalUse"/&gt;
 *     &lt;enumeration value="LowAlt"/&gt;
 *     &lt;enumeration value="HighAlt"/&gt;
 *     &lt;enumeration value="ExtendedHighAlt"/&gt;
 *     &lt;enumeration value="NotNOTAMd"/&gt;
 *     &lt;enumeration value="OutOfService"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FigureOfMerit", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FigureOfMerit {


    /**
     * Terminal Use (generally within 25NM)
     * 
     */
    @XmlEnumValue("TerminalUse")
    TERMINAL_USE("TerminalUse"),

    /**
     * Low Altitude Use (generally within 40NM)
     * 
     */
    @XmlEnumValue("LowAlt")
    LOW_ALT("LowAlt"),

    /**
     * High Altitude Use (generally within 130NM)
     * 
     */
    @XmlEnumValue("HighAlt")
    HIGH_ALT("HighAlt"),

    /**
     * Extended High Altitude Use (generally beyond 130NM)
     * 
     */
    @XmlEnumValue("ExtendedHighAlt")
    EXTENDED_HIGH_ALT("ExtendedHighAlt"),

    /**
     * Navaid Not included in a civil international NOTAM system
     * 
     */
    @XmlEnumValue("NotNOTAMd")
    NOT_NOTA_MD("NotNOTAMd"),

    /**
     * Navaid Out of Service
     * 
     */
    @XmlEnumValue("OutOfService")
    OUT_OF_SERVICE("OutOfService");
    private final String value;

    FigureOfMerit(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FigureOfMerit fromValue(String v) {
        for (FigureOfMerit c: FigureOfMerit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
