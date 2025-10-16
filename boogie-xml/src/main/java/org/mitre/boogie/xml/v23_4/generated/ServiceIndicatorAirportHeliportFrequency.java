
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceIndicatorAirportHeliportFrequency.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceIndicatorAirportHeliportFrequency"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ATF"/&gt;
 *     &lt;enumeration value="CTAF"/&gt;
 *     &lt;enumeration value="MF"/&gt;
 *     &lt;enumeration value="SF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceIndicatorAirportHeliportFrequency", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ServiceIndicatorAirportHeliportFrequency {


    /**
     * Aerodrome Traffic Frequency (ATF)
     * 
     */
    ATF,

    /**
     * Common Traffic Advisory Frequency (CTAF)
     * 
     */
    CTAF,

    /**
     * Mandatory Frequency (MF)
     * 
     */
    MF,

    /**
     * Secondary Frequency
     * 
     */
    SF;

    public String value() {
        return name();
    }

    public static ServiceIndicatorAirportHeliportFrequency fromValue(String v) {
        return valueOf(v);
    }

}
