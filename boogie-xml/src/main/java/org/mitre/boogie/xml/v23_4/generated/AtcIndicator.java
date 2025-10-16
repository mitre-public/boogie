
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AtcIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AtcIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ModifiedOrAssigned"/&gt;
 *     &lt;enumeration value="AssignedIfNotProvided"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AtcIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AtcIndicator {


    /**
     * Altitude can be modified or assigned by ATC.
     * 
     */
    @XmlEnumValue("ModifiedOrAssigned")
    MODIFIED_OR_ASSIGNED("ModifiedOrAssigned"),

    /**
     * official government source states that the altitude will be assigned by ATC or if no altitude is supplied
     * 
     */
    @XmlEnumValue("AssignedIfNotProvided")
    ASSIGNED_IF_NOT_PROVIDED("AssignedIfNotProvided");
    private final String value;

    AtcIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AtcIndicator fromValue(String v) {
        for (AtcIndicator c: AtcIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
