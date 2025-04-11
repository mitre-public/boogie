
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LocalizerAzimuthPositionReference.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LocalizerAzimuthPositionReference"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BeyondStopEnd"/&gt;
 *     &lt;enumeration value="BeforeApproachEnd"/&gt;
 *     &lt;enumeration value="OffToSide"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LocalizerAzimuthPositionReference", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum LocalizerAzimuthPositionReference {


    /**
     * For Localizer and Azimuth positions the field is blank (@) when the antenna is situated beyond the stop end of the runway,
     * For Back Azimuth positions the field is blank (@) when the antenna is situated ahead of the approach end of the runway
     * 
     */
    @XmlEnumValue("BeyondStopEnd")
    BEYOND_STOP_END("BeyondStopEnd"),

    /**
     * For Localizer and Azimuth positions the field contains a plus (+) sign when the antenna is situated ahead of the approach end of the runway
     * For Back Azimuth positions the field contains a plus (+) sign when the antenna is situated beyond the stop end of the runway
     * 
     */
    @XmlEnumValue("BeforeApproachEnd")
    BEFORE_APPROACH_END("BeforeApproachEnd"),

    /**
     * For Localizer and Azimuth positions the field contains a plus (-) sign when the antenna is located off to one side of the runway
     * For Back Azimuth positions the field contains a plus (-) sign when it is located off to one side of the runway.
     * 
     */
    @XmlEnumValue("OffToSide")
    OFF_TO_SIDE("OffToSide");
    private final String value;

    LocalizerAzimuthPositionReference(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LocalizerAzimuthPositionReference fromValue(String v) {
        for (LocalizerAzimuthPositionReference c: LocalizerAzimuthPositionReference.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
