
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceIndicatorEnrouteInformation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceIndicatorEnrouteInformation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Vdf"/&gt;
 *     &lt;enumeration value="NotEnglish"/&gt;
 *     &lt;enumeration value="MilitaryUseFreq"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceIndicatorEnrouteInformation", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ServiceIndicatorEnrouteInformation {


    /**
     * VHF Direction Finding Service (VDF)
     * 
     */
    @XmlEnumValue("Vdf")
    VDF("Vdf"),

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
    MILITARY_USE_FREQ("MilitaryUseFreq");
    private final String value;

    ServiceIndicatorEnrouteInformation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServiceIndicatorEnrouteInformation fromValue(String v) {
        for (ServiceIndicatorEnrouteInformation c: ServiceIndicatorEnrouteInformation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
