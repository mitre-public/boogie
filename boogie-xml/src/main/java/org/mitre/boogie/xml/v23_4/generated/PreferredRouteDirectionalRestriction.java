
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PreferredRouteDirectionalRestriction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PreferredRouteDirectionalRestriction"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="UniDirectional"/&gt;
 *     &lt;enumeration value="BiDirectional"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PreferredRouteDirectionalRestriction", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PreferredRouteDirectionalRestriction {


    /**
     * Uni-directional routing, usable only from Initial Fix to Terminus Fix.
     * 
     */
    @XmlEnumValue("UniDirectional")
    UNI_DIRECTIONAL("UniDirectional"),

    /**
     * Bi-directional routing, usable from Initial Fix to Terminus Fix or from Terminus Fix to Initial Fix.
     * 
     */
    @XmlEnumValue("BiDirectional")
    BI_DIRECTIONAL("BiDirectional");
    private final String value;

    PreferredRouteDirectionalRestriction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PreferredRouteDirectionalRestriction fromValue(String v) {
        for (PreferredRouteDirectionalRestriction c: PreferredRouteDirectionalRestriction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
