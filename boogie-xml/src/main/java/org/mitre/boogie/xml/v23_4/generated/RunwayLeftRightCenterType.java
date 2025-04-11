
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RunwayLeftRightCenterType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RunwayLeftRightCenterType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Left"/&gt;
 *     &lt;enumeration value="Right"/&gt;
 *     &lt;enumeration value="Center"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RunwayLeftRightCenterType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RunwayLeftRightCenterType {


    /**
     * Left (Runway of two or three parallel runways)
     * 
     */
    @XmlEnumValue("Left")
    LEFT("Left"),

    /**
     * Right (Runway of two or three parallel runways)
     * 
     */
    @XmlEnumValue("Right")
    RIGHT("Right"),

    /**
     * Center (Runway of three parallel runways)
     * 
     */
    @XmlEnumValue("Center")
    CENTER("Center");
    private final String value;

    RunwayLeftRightCenterType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RunwayLeftRightCenterType fromValue(String v) {
        for (RunwayLeftRightCenterType c: RunwayLeftRightCenterType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
