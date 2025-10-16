
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AhLocalizerMarkerLocatorAddInfo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AhLocalizerMarkerLocatorAddInfo"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NoVoiceOnFrequency"/&gt;
 *     &lt;enumeration value="VoiceOnFrequency"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AhLocalizerMarkerLocatorAddInfo", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AhLocalizerMarkerLocatorAddInfo {


    /**
     * No Voice on Frequency.The frequency of this Navaid is not used to support two-way communication between a ground station and aircraft.
     * 
     */
    @XmlEnumValue("NoVoiceOnFrequency")
    NO_VOICE_ON_FREQUENCY("NoVoiceOnFrequency"),

    /**
     * Voice on Frequency.The frequency of this Navaid is used to support two-way communication between a ground station and aircraft.
     * 
     */
    @XmlEnumValue("VoiceOnFrequency")
    VOICE_ON_FREQUENCY("VoiceOnFrequency");
    private final String value;

    AhLocalizerMarkerLocatorAddInfo(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AhLocalizerMarkerLocatorAddInfo fromValue(String v) {
        for (AhLocalizerMarkerLocatorAddInfo c: AhLocalizerMarkerLocatorAddInfo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
