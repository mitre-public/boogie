
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NavaidNdbEmissionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NavaidNdbEmissionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="UnmodulatedCarrier"/&gt;
 *     &lt;enumeration value="CarrierKeyed"/&gt;
 *     &lt;enumeration value="ToneKeyModulation"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NavaidNdbEmissionType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NavaidNdbEmissionType {


    /**
     * Unmodulated carrier
     * 
     */
    @XmlEnumValue("UnmodulatedCarrier")
    UNMODULATED_CARRIER("UnmodulatedCarrier"),

    /**
     * Carrier keyed, bandwidth less than .1 kHz and/or carrier keyed, bandwidth greater than .1 kHz
     * 
     */
    @XmlEnumValue("CarrierKeyed")
    CARRIER_KEYED("CarrierKeyed"),

    /**
     * Tone keyed modulation
     * 
     */
    @XmlEnumValue("ToneKeyModulation")
    TONE_KEY_MODULATION("ToneKeyModulation");
    private final String value;

    NavaidNdbEmissionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NavaidNdbEmissionType fromValue(String v) {
        for (NavaidNdbEmissionType c: NavaidNdbEmissionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
