
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MlsDmeLocation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MlsDmeLocation"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CollocatedAzimuth"/&gt;
 *     &lt;enumeration value="CollocatedElevation"/&gt;
 *     &lt;enumeration value="NotCollocated"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MlsDmeLocation", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MlsDmeLocation {


    /**
     * Collocated with Azimuth
     * 
     */
    @XmlEnumValue("CollocatedAzimuth")
    COLLOCATED_AZIMUTH("CollocatedAzimuth"),

    /**
     * Collocated with Elevation.
     * 
     */
    @XmlEnumValue("CollocatedElevation")
    COLLOCATED_ELEVATION("CollocatedElevation"),

    /**
     * Not Collocated with Azimuth or Elevation
     * 
     */
    @XmlEnumValue("NotCollocated")
    NOT_COLLOCATED("NotCollocated");
    private final String value;

    MlsDmeLocation(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MlsDmeLocation fromValue(String v) {
        for (MlsDmeLocation c: MlsDmeLocation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
