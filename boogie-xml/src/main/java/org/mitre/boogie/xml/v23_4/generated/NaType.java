
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NaType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NotApplicable"/&gt;
 *     &lt;enumeration value="NotAuthorized"/&gt;
 *     &lt;enumeration value="NotAvailable"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NaType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NaType {


    /**
     * NA meaning is "not applicable"
     * 
     */
    @XmlEnumValue("NotApplicable")
    NOT_APPLICABLE("NotApplicable"),

    /**
     * NA meaning is "not authorized"
     * 
     */
    @XmlEnumValue("NotAuthorized")
    NOT_AUTHORIZED("NotAuthorized"),

    /**
     * NA meaning is "not available"
     * 
     */
    @XmlEnumValue("NotAvailable")
    NOT_AVAILABLE("NotAvailable");
    private final String value;

    NaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NaType fromValue(String v) {
        for (NaType c: NaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
