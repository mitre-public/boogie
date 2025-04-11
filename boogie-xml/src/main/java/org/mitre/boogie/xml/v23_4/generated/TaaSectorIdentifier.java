
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaaSectorIdentifier.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TaaSectorIdentifier"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="StraightInOrCenterFix"/&gt;
 *     &lt;enumeration value="LeftBase"/&gt;
 *     &lt;enumeration value="RightBase"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TaaSectorIdentifier", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum TaaSectorIdentifier {


    /**
     * Straight-In or Center Fix
     * 
     */
    @XmlEnumValue("StraightInOrCenterFix")
    STRAIGHT_IN_OR_CENTER_FIX("StraightInOrCenterFix"),

    /**
     * Left Base Area
     * 
     */
    @XmlEnumValue("LeftBase")
    LEFT_BASE("LeftBase"),

    /**
     * Right Base Area
     * 
     */
    @XmlEnumValue("RightBase")
    RIGHT_BASE("RightBase");
    private final String value;

    TaaSectorIdentifier(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TaaSectorIdentifier fromValue(String v) {
        for (TaaSectorIdentifier c: TaaSectorIdentifier.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
