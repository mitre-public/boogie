
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UseIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UseIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Implemented"/&gt;
 *     &lt;enumeration value="Future"/&gt;
 *     &lt;enumeration value="TestFacility"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UseIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum UseIndicator {

    @XmlEnumValue("Implemented")
    IMPLEMENTED("Implemented"),
    @XmlEnumValue("Future")
    FUTURE("Future"),
    @XmlEnumValue("TestFacility")
    TEST_FACILITY("TestFacility"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    UseIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UseIndicator fromValue(String v) {
        for (UseIndicator c: UseIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
