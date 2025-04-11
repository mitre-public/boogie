
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TrueBearingSource.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TrueBearingSource"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Official"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TrueBearingSource", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TrueBearingSource {


    /**
     * “True Bearing” is derived from official government sources.
     * 
     */
    @XmlEnumValue("Official")
    OFFICIAL("Official"),

    /**
     * “True Bearing" is derived from other sources
     * 
     */
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    TrueBearingSource(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TrueBearingSource fromValue(String v) {
        for (TrueBearingSource c: TrueBearingSource.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
