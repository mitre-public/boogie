
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EuIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="EuIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NoRestriction"/&gt;
 *     &lt;enumeration value="Restriction"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EuIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum EuIndicator {


    /**
     * no restriction record exists.
     * 
     */
    @XmlEnumValue("NoRestriction")
    NO_RESTRICTION("NoRestriction"),

    /**
     * restriction for the segment is contained in the restriction file
     * 
     */
    @XmlEnumValue("Restriction")
    RESTRICTION("Restriction");
    private final String value;

    EuIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EuIndicator fromValue(String v) {
        for (EuIndicator c: EuIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
