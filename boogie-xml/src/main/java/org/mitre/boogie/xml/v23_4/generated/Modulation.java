
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Modulation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Modulation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AmFreq"/&gt;
 *     &lt;enumeration value="FmFreq"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Modulation", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum Modulation {


    /**
     * Amplitude Modulated Frequency
     * 
     */
    @XmlEnumValue("AmFreq")
    AM_FREQ("AmFreq"),

    /**
     * Frequency Modulated Frequency
     * 
     */
    @XmlEnumValue("FmFreq")
    FM_FREQ("FmFreq");
    private final String value;

    Modulation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Modulation fromValue(String v) {
        for (Modulation c: Modulation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
