
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FrequencyUnits.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FrequencyUnits"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LowFreq"/&gt;
 *     &lt;enumeration value="MediumFreq"/&gt;
 *     &lt;enumeration value="HighFreq"/&gt;
 *     &lt;enumeration value="Vhf100kHzSpacing"/&gt;
 *     &lt;enumeration value="Vhf50kHzSpacing"/&gt;
 *     &lt;enumeration value="Vhf25kHzSpacing"/&gt;
 *     &lt;enumeration value="VhfNonStandard"/&gt;
 *     &lt;enumeration value="Uhf"/&gt;
 *     &lt;enumeration value="Vhf8_33Spacing"/&gt;
 *     &lt;enumeration value="DigitalService"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FrequencyUnits", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FrequencyUnits {


    /**
     * Low Frequency
     * 
     */
    @XmlEnumValue("LowFreq")
    LOW_FREQ("LowFreq"),

    /**
     * Medium Frequency
     * 
     */
    @XmlEnumValue("MediumFreq")
    MEDIUM_FREQ("MediumFreq"),

    /**
     * High Frequency(3000 kHz to 30,000 kHz)
     * 
     */
    @XmlEnumValue("HighFreq")
    HIGH_FREQ("HighFreq"),

    /**
     * Very High Frequency 100 kHz spacing
     * 
     */
    @XmlEnumValue("Vhf100kHzSpacing")
    VHF_100_K_HZ_SPACING("Vhf100kHzSpacing"),

    /**
     * Very High Frequency 50 kHz spacing
     * 
     */
    @XmlEnumValue("Vhf50kHzSpacing")
    VHF_50_K_HZ_SPACING("Vhf50kHzSpacing"),

    /**
     * Very High Frequency 25 kHz spacing
     * 
     */
    @XmlEnumValue("Vhf25kHzSpacing")
    VHF_25_K_HZ_SPACING("Vhf25kHzSpacing"),

    /**
     * Very High Frequency (30,000 kHz to 200 MHz) Non-standard spacing
     * 
     */
    @XmlEnumValue("VhfNonStandard")
    VHF_NON_STANDARD("VhfNonStandard"),

    /**
     * Ultra High Frequency (200 MHz to 3000 MHz)
     * 
     */
    @XmlEnumValue("Uhf")
    UHF("Uhf"),

    /**
     * Very High Frequency Communication Channel for 8.33kHz spacing
     * 
     */
    @XmlEnumValue("Vhf8_33Spacing")
    VHF_8_33_SPACING("Vhf8_33Spacing"),

    /**
     * Digital Service
     * 
     */
    @XmlEnumValue("DigitalService")
    DIGITAL_SERVICE("DigitalService");
    private final String value;

    FrequencyUnits(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FrequencyUnits fromValue(String v) {
        for (FrequencyUnits c: FrequencyUnits.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
