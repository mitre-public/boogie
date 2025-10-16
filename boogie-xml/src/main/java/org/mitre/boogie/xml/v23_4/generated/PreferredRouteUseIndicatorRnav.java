
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PreferredRouteUseIndicatorRnav.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PreferredRouteUseIndicatorRnav"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="RnavEquipRequired"/&gt;
 *     &lt;enumeration value="RnavEquipNotRequired"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PreferredRouteUseIndicatorRnav", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PreferredRouteUseIndicatorRnav {


    /**
     * RNAV equipment is required
     * 
     */
    @XmlEnumValue("RnavEquipRequired")
    RNAV_EQUIP_REQUIRED("RnavEquipRequired"),

    /**
     * RNAV equipment is NOT required
     * 
     */
    @XmlEnumValue("RnavEquipNotRequired")
    RNAV_EQUIP_NOT_REQUIRED("RnavEquipNotRequired");
    private final String value;

    PreferredRouteUseIndicatorRnav(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PreferredRouteUseIndicatorRnav fromValue(String v) {
        for (PreferredRouteUseIndicatorRnav c: PreferredRouteUseIndicatorRnav.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
