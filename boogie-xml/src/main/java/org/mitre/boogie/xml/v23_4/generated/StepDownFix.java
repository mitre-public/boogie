
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StepDownFix.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="StepDownFix"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="unnamedInFinalApproachSegment"/&gt;
 *     &lt;enumeration value="unnamedInIntermediateApproachSegment"/&gt;
 *     &lt;enumeration value="named"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StepDownFix", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum StepDownFix {

    @XmlEnumValue("unnamedInFinalApproachSegment")
    UNNAMED_IN_FINAL_APPROACH_SEGMENT("unnamedInFinalApproachSegment"),
    @XmlEnumValue("unnamedInIntermediateApproachSegment")
    UNNAMED_IN_INTERMEDIATE_APPROACH_SEGMENT("unnamedInIntermediateApproachSegment"),
    @XmlEnumValue("named")
    NAMED("named");
    private final String value;

    StepDownFix(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StepDownFix fromValue(String v) {
        for (StepDownFix c: StepDownFix.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
