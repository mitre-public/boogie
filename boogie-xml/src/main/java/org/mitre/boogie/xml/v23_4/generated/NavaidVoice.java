
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NavaidVoice.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NavaidVoice"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="VoiceIdent"/&gt;
 *     &lt;enumeration value="NoVoiceIdent"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NavaidVoice", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NavaidVoice {


    /**
     * Voice Ident
     * 
     */
    @XmlEnumValue("VoiceIdent")
    VOICE_IDENT("VoiceIdent"),

    /**
     * No Voice Ident
     * 
     */
    @XmlEnumValue("NoVoiceIdent")
    NO_VOICE_IDENT("NoVoiceIdent"),

    /**
     * Undefined
     * 
     */
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined");
    private final String value;

    NavaidVoice(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NavaidVoice fromValue(String v) {
        for (NavaidVoice c: NavaidVoice.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
