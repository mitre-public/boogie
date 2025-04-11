
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompanyRouteViaCodes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="CompanyRouteViaCodes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ALT"/&gt;
 *     &lt;enumeration value="APP"/&gt;
 *     &lt;enumeration value="APT"/&gt;
 *     &lt;enumeration value="AWY"/&gt;
 *     &lt;enumeration value="DIR"/&gt;
 *     &lt;enumeration value="INT"/&gt;
 *     &lt;enumeration value="PRE"/&gt;
 *     &lt;enumeration value="SID"/&gt;
 *     &lt;enumeration value="SDE"/&gt;
 *     &lt;enumeration value="SDY"/&gt;
 *     &lt;enumeration value="STR"/&gt;
 *     &lt;enumeration value="STE"/&gt;
 *     &lt;enumeration value="STY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CompanyRouteViaCodes", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum CompanyRouteViaCodes {


    /**
     * Alternate Airport
     * 
     */
    ALT,

    /**
     * Approach Route
     * 
     */
    APP,

    /**
     * Approach Transition
     * 
     */
    APT,

    /**
     * Designated Airway
     * 
     */
    AWY,

    /**
     * Direct to Fix
     * 
     */
    DIR,

    /**
     * Initial Fix
     * 
     */
    INT,

    /**
     * Preferred Route
     * 
     */
    PRE,

    /**
     * Standard Instrument Departure
     * 
     */
    SID,

    /**
     * Standard Instrument Departure - Enroute Transition
     * 
     */
    SDE,

    /**
     * Standard Instrument Departure - Runway Transition
     * 
     */
    SDY,

    /**
     * Standard Terminal Arrival and Profile Descent
     * 
     */
    STR,

    /**
     * Standard Terminal Arrival and Profile Descent - Enroute Transition
     * 
     */
    STE,

    /**
     * Standard Terminal Arrival and Profile Descent - Runway Transition
     * 
     */
    STY;

    public String value() {
        return name();
    }

    public static CompanyRouteViaCodes fromValue(String v) {
        return valueOf(v);
    }

}
