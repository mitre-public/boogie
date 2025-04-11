
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceIndicatorEnrouteFrequency.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceIndicatorEnrouteFrequency"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AirGround"/&gt;
 *     &lt;enumeration value="Distance"/&gt;
 *     &lt;enumeration value="MF"/&gt;
 *     &lt;enumeration value="SF"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceIndicatorEnrouteFrequency", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ServiceIndicatorEnrouteFrequency {


    /**
     * Air/Ground
     * 
     */
    @XmlEnumValue("AirGround")
    AIR_GROUND("AirGround"),

    /**
     * Discrete Frequency
     * 
     */
    @XmlEnumValue("Distance")
    DISTANCE("Distance"),

    /**
     * Mandatory Frequency (MF)
     * 
     */
    MF("MF"),

    /**
     * Secondary Frequency
     * 
     */
    SF("SF");
    private final String value;

    ServiceIndicatorEnrouteFrequency(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServiceIndicatorEnrouteFrequency fromValue(String v) {
        for (ServiceIndicatorEnrouteFrequency c: ServiceIndicatorEnrouteFrequency.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
