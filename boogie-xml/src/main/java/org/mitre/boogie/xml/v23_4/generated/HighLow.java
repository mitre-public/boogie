
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HighLow.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="HighLow"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Low"/&gt;
 *     &lt;enumeration value="High"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HighLow", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum HighLow {


    /**
     * Low Power
     * 
     */
    @XmlEnumValue("Low")
    LOW("Low"),

    /**
     * High Power
     * 
     */
    @XmlEnumValue("High")
    HIGH("High");
    private final String value;

    HighLow(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HighLow fromValue(String v) {
        for (HighLow c: HighLow.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
