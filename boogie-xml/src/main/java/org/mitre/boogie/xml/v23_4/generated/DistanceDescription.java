
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DistanceDescription.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="DistanceDescription"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OutToSpecifiedDistance"/&gt;
 *     &lt;enumeration value="AppliesBeyondDistance"/&gt;
 *     &lt;enumeration value="NoRestriction"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DistanceDescription", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum DistanceDescription {


    /**
     * The communication frequency or navaid limitation is out to a specified distance.
     * 
     */
    @XmlEnumValue("OutToSpecifiedDistance")
    OUT_TO_SPECIFIED_DISTANCE("OutToSpecifiedDistance"),

    /**
     * The communication frequency is used or the navaid limitation applies beyond a specified distance
     * 
     */
    @XmlEnumValue("AppliesBeyondDistance")
    APPLIES_BEYOND_DISTANCE("AppliesBeyondDistance"),

    /**
     * no restrictions/limitations apply
     * 
     */
    @XmlEnumValue("NoRestriction")
    NO_RESTRICTION("NoRestriction");
    private final String value;

    DistanceDescription(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DistanceDescription fromValue(String v) {
        for (DistanceDescription c: DistanceDescription.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
