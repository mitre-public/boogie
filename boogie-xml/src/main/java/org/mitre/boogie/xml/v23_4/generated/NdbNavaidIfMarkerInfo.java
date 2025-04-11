
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NdbNavaidIfMarkerInfo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NdbNavaidIfMarkerInfo"&gt;
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
@XmlType(name = "NdbNavaidIfMarkerInfo", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NdbNavaidIfMarkerInfo {


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

    NdbNavaidIfMarkerInfo(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NdbNavaidIfMarkerInfo fromValue(String v) {
        for (NdbNavaidIfMarkerInfo c: NdbNavaidIfMarkerInfo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
