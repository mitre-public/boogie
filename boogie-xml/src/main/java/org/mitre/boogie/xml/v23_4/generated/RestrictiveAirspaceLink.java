
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RestrictiveAirspaceLink.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RestrictiveAirspaceLink"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AddNotReq"/&gt;
 *     &lt;enumeration value="AddIsReq"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RestrictiveAirspaceLink", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RestrictiveAirspaceLink {


    /**
     * additional Continuation Record is not required
     * 
     */
    @XmlEnumValue("AddNotReq")
    ADD_NOT_REQ("AddNotReq"),

    /**
     * additional Continuation Record is required
     * 
     */
    @XmlEnumValue("AddIsReq")
    ADD_IS_REQ("AddIsReq");
    private final String value;

    RestrictiveAirspaceLink(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RestrictiveAirspaceLink fromValue(String v) {
        for (RestrictiveAirspaceLink c: RestrictiveAirspaceLink.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
