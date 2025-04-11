
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RecordType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Standard"/&gt;
 *     &lt;enumeration value="Tailored"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RecordType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RecordType {


    /**
     * Standard
     * 
     */
    @XmlEnumValue("Standard")
    STANDARD("Standard"),

    /**
     * Tailored
     * 
     */
    @XmlEnumValue("Tailored")
    TAILORED("Tailored");
    private final String value;

    RecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RecordType fromValue(String v) {
        for (RecordType c: RecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
