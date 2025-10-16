
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApproachQualifier2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ApproachQualifier2"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PrimaryMissedApproach"/&gt;
 *     &lt;enumeration value="SecondaryMissedApproach"/&gt;
 *     &lt;enumeration value="EngineOutMissedApproach"/&gt;
 *     &lt;enumeration value="CircleToLandMinimums"/&gt;
 *     &lt;enumeration value="HelicopterStraightInMinimums"/&gt;
 *     &lt;enumeration value="HelicopterCircleToLandMinimums"/&gt;
 *     &lt;enumeration value="HelicopterLandingMinimums"/&gt;
 *     &lt;enumeration value="StraightInMinimums"/&gt;
 *     &lt;enumeration value="VmcMinimums"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ApproachQualifier2", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ApproachQualifier2 {


    /**
     * Primary Missed Approach
     * 
     */
    @XmlEnumValue("PrimaryMissedApproach")
    PRIMARY_MISSED_APPROACH("PrimaryMissedApproach"),

    /**
     * Secondary Missed Approach
     * 
     */
    @XmlEnumValue("SecondaryMissedApproach")
    SECONDARY_MISSED_APPROACH("SecondaryMissedApproach"),

    /**
     * Engine Out Missed Approach
     * 
     */
    @XmlEnumValue("EngineOutMissedApproach")
    ENGINE_OUT_MISSED_APPROACH("EngineOutMissedApproach"),

    /**
     * Procedure with Circle-To-Land Minimums
     * 
     */
    @XmlEnumValue("CircleToLandMinimums")
    CIRCLE_TO_LAND_MINIMUMS("CircleToLandMinimums"),

    /**
     * Helicopter with straight-in minimums
     * 
     */
    @XmlEnumValue("HelicopterStraightInMinimums")
    HELICOPTER_STRAIGHT_IN_MINIMUMS("HelicopterStraightInMinimums"),

    /**
     * Helicopter with circle-to-land minimums
     * 
     */
    @XmlEnumValue("HelicopterCircleToLandMinimums")
    HELICOPTER_CIRCLE_TO_LAND_MINIMUMS("HelicopterCircleToLandMinimums"),

    /**
     * Helicopter with Helicopter Landing Minimums
     * 
     */
    @XmlEnumValue("HelicopterLandingMinimums")
    HELICOPTER_LANDING_MINIMUMS("HelicopterLandingMinimums"),

    /**
     * Procedure with Straight-in Minimums
     * 
     */
    @XmlEnumValue("StraightInMinimums")
    STRAIGHT_IN_MINIMUMS("StraightInMinimums"),

    /**
     * Procedure with VMC minimums
     * 
     */
    @XmlEnumValue("VmcMinimums")
    VMC_MINIMUMS("VmcMinimums");
    private final String value;

    ApproachQualifier2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ApproachQualifier2 fromValue(String v) {
        for (ApproachQualifier2 c: ApproachQualifier2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
