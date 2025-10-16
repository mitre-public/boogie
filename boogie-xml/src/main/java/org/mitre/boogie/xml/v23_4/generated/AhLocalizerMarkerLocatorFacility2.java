
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AhLocalizerMarkerLocatorFacility2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AhLocalizerMarkerLocatorFacility2"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="InnerMarker"/&gt;
 *     &lt;enumeration value="MiddleMarker"/&gt;
 *     &lt;enumeration value="OuterMarker"/&gt;
 *     &lt;enumeration value="BackMarker"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AhLocalizerMarkerLocatorFacility2", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AhLocalizerMarkerLocatorFacility2 {


    /**
     * Inner Marker. There is an Inner Marker beacon at this location.
     * 
     */
    @XmlEnumValue("InnerMarker")
    INNER_MARKER("InnerMarker"),

    /**
     * Middle Marker. There is a Middle Marker beacon at this location.
     * 
     */
    @XmlEnumValue("MiddleMarker")
    MIDDLE_MARKER("MiddleMarker"),

    /**
     * Outer Marker. There is an Outer Marker beacon at this location.
     * 
     */
    @XmlEnumValue("OuterMarker")
    OUTER_MARKER("OuterMarker"),

    /**
     * Back Marker. There is a Backcourse Marker at this location.
     * 
     */
    @XmlEnumValue("BackMarker")
    BACK_MARKER("BackMarker");
    private final String value;

    AhLocalizerMarkerLocatorFacility2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AhLocalizerMarkerLocatorFacility2 fromValue(String v) {
        for (AhLocalizerMarkerLocatorFacility2 c: AhLocalizerMarkerLocatorFacility2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
