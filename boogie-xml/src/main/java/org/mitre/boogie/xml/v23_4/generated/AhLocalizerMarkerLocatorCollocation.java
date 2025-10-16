
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AhLocalizerMarkerLocatorCollocation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AhLocalizerMarkerLocatorCollocation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BfoOperation"/&gt;
 *     &lt;enumeration value="LocatorMarkerCollocated"/&gt;
 *     &lt;enumeration value="LocatorMiddleMarkerNotCollocated"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AhLocalizerMarkerLocatorCollocation", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AhLocalizerMarkerLocatorCollocation {


    /**
     * BFO Operation. Use of Beat Frequency Oscillator type of equipment is required to received an aural identification signal.
     * 
     */
    @XmlEnumValue("BfoOperation")
    BFO_OPERATION("BfoOperation"),

    /**
     * Locator/Marker Collocated. The latitude/longitude position of the Locator and Marker are identical.
     * 
     */
    @XmlEnumValue("LocatorMarkerCollocated")
    LOCATOR_MARKER_COLLOCATED("LocatorMarkerCollocated"),

    /**
     * Locator/Middle Marker Not Collocated. The latitude/longitude position of Locator and Marker are not identical.
     * 
     */
    @XmlEnumValue("LocatorMiddleMarkerNotCollocated")
    LOCATOR_MIDDLE_MARKER_NOT_COLLOCATED("LocatorMiddleMarkerNotCollocated");
    private final String value;

    AhLocalizerMarkerLocatorCollocation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AhLocalizerMarkerLocatorCollocation fromValue(String v) {
        for (AhLocalizerMarkerLocatorCollocation c: AhLocalizerMarkerLocatorCollocation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
