
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PathAndTermination.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PathAndTermination"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AF"/&gt;
 *     &lt;enumeration value="CA"/&gt;
 *     &lt;enumeration value="CD"/&gt;
 *     &lt;enumeration value="CF"/&gt;
 *     &lt;enumeration value="CI"/&gt;
 *     &lt;enumeration value="CR"/&gt;
 *     &lt;enumeration value="FA"/&gt;
 *     &lt;enumeration value="DF"/&gt;
 *     &lt;enumeration value="FC"/&gt;
 *     &lt;enumeration value="FD"/&gt;
 *     &lt;enumeration value="FM"/&gt;
 *     &lt;enumeration value="HA"/&gt;
 *     &lt;enumeration value="HF"/&gt;
 *     &lt;enumeration value="HM"/&gt;
 *     &lt;enumeration value="IF"/&gt;
 *     &lt;enumeration value="PI"/&gt;
 *     &lt;enumeration value="RF"/&gt;
 *     &lt;enumeration value="TF"/&gt;
 *     &lt;enumeration value="VA"/&gt;
 *     &lt;enumeration value="VD"/&gt;
 *     &lt;enumeration value="VI"/&gt;
 *     &lt;enumeration value="VM"/&gt;
 *     &lt;enumeration value="VR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PathAndTermination", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PathAndTermination {


    /**
     * Arc to a Fix
     * 
     */
    AF,

    /**
     * Course to an Altitude
     * 
     */
    CA,

    /**
     * Course to a DME Distance
     * 
     */
    CD,

    /**
     * Course to a Fix
     * 
     */
    CF,

    /**
     * Course to an Intercept
     * 
     */
    CI,

    /**
     * Course to a Radial Termination
     * 
     */
    CR,

    /**
     * Fix to an Altitude
     * 
     */
    FA,

    /**
     * Distance to a Fix
     * 
     */
    DF,

    /**
     * Track from a Fix for a Distance
     * 
     */
    FC,

    /**
     * Track from a Fix to a DME Distance
     * 
     */
    FD,

    /**
     * From a Fix to a Manual Termination
     * 
     */
    FM,

    /**
     * Holding with Altitude Termination
     * 
     */
    HA,

    /**
     * Holding with single circuit terminating at the fix
     * 
     */
    HF,

    /**
     * Holding with Manual Termination
     * 
     */
    HM,

    /**
     * Initial Fix
     * 
     */
    IF,

    /**
     *  045/180 Procedure Turn
     * 
     */
    PI,

    /**
     * Constant Radius Arc
     * 
     */
    RF,

    /**
     * Track to a Fix
     * 
     */
    TF,

    /**
     * Heading to an Altitude Termination
     * 
     */
    VA,

    /**
     * Heading to a DME Distance Termination
     * 
     */
    VD,

    /**
     * Heading to an Intercept
     * 
     */
    VI,

    /**
     * Heading to a Manual Termination
     * 
     */
    VM,

    /**
     * Heading to a Radial Termination
     * 
     */
    VR;

    public String value() {
        return name();
    }

    public static PathAndTermination fromValue(String v) {
        return valueOf(v);
    }

}
