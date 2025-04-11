
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PreferredRouteViaCodes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PreferredRouteViaCodes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AWY"/&gt;
 *     &lt;enumeration value="DIR"/&gt;
 *     &lt;enumeration value="INT"/&gt;
 *     &lt;enumeration value="RVF"/&gt;
 *     &lt;enumeration value="RNF"/&gt;
 *     &lt;enumeration value="SID"/&gt;
 *     &lt;enumeration value="STR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PreferredRouteViaCodes", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PreferredRouteViaCodes {


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
     * Route via Fix
     * 
     */
    RVF,

    /**
     * Route via Fix not permitted
     * 
     */
    RNF,

    /**
     * Standard Instrument Departure
     * 
     */
    SID,

    /**
     * Standard Terminal Arrival and Profile Descent
     * 
     */
    STR;

    public String value() {
        return name();
    }

    public static PreferredRouteViaCodes fromValue(String v) {
        return valueOf(v);
    }

}
