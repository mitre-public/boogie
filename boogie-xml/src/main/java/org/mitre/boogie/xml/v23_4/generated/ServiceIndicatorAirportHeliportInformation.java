
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceIndicatorAirportHeliportInformation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceIndicatorAirportHeliportInformation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="VDF"/&gt;
 *     &lt;enumeration value="NotEnglish"/&gt;
 *     &lt;enumeration value="MilitaryUseFreq"/&gt;
 *     &lt;enumeration value="PCL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceIndicatorAirportHeliportInformation", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ServiceIndicatorAirportHeliportInformation {


    /**
     * VHF Direction Finding Service (VDF)
     * 
     */
    VDF("VDF"),

    /**
     * Language other than English
     * 
     */
    @XmlEnumValue("NotEnglish")
    NOT_ENGLISH("NotEnglish"),

    /**
     * Military Use Frequency
     * 
     */
    @XmlEnumValue("MilitaryUseFreq")
    MILITARY_USE_FREQ("MilitaryUseFreq"),

    /**
     * Pilot Controlled Light (PCL)
     * 
     */
    PCL("PCL");
    private final String value;

    ServiceIndicatorAirportHeliportInformation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServiceIndicatorAirportHeliportInformation fromValue(String v) {
        for (ServiceIndicatorAirportHeliportInformation c: ServiceIndicatorAirportHeliportInformation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
