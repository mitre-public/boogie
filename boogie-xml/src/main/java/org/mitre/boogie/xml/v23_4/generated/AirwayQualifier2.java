
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirwayQualifier2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirwayQualifier2"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FixRelatedTransitionRequired"/&gt;
 *     &lt;enumeration value="ParallelOffsetRequired"/&gt;
 *     &lt;enumeration value="ToacRequired"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirwayQualifier2", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AirwayQualifier2 {


    /**
     * Fix Related Transition (FRT) required
     * 
     */
    @XmlEnumValue("FixRelatedTransitionRequired")
    FIX_RELATED_TRANSITION_REQUIRED("FixRelatedTransitionRequired"),

    /**
     * Parallel Offset Required
     * 
     */
    @XmlEnumValue("ParallelOffsetRequired")
    PARALLEL_OFFSET_REQUIRED("ParallelOffsetRequired"),

    /**
     * TOAC Required
     * 
     */
    @XmlEnumValue("ToacRequired")
    TOAC_REQUIRED("ToacRequired");
    private final String value;

    AirwayQualifier2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AirwayQualifier2 fromValue(String v) {
        for (AirwayQualifier2 c: AirwayQualifier2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
