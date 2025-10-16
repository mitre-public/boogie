
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StepClimbIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="StepClimbIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="StepUpDownPermitted"/&gt;
 *     &lt;enumeration value="StepDownPermitted"/&gt;
 *     &lt;enumeration value="NoStepPermitted"/&gt;
 *     &lt;enumeration value="OnlyStepUpPermitted"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StepClimbIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum StepClimbIndicator {


    /**
     * Step climb up or down is permitted
     * 
     */
    @XmlEnumValue("StepUpDownPermitted")
    STEP_UP_DOWN_PERMITTED("StepUpDownPermitted"),

    /**
     * Only step climb down is permitted
     * 
     */
    @XmlEnumValue("StepDownPermitted")
    STEP_DOWN_PERMITTED("StepDownPermitted"),

    /**
     * No step climb is permitted
     * 
     */
    @XmlEnumValue("NoStepPermitted")
    NO_STEP_PERMITTED("NoStepPermitted"),

    /**
     * Only step climb up is permitted
     * 
     */
    @XmlEnumValue("OnlyStepUpPermitted")
    ONLY_STEP_UP_PERMITTED("OnlyStepUpPermitted");
    private final String value;

    StepClimbIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StepClimbIndicator fromValue(String v) {
        for (StepClimbIndicator c: StepClimbIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
