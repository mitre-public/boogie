
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StationDeclinationEWT.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="StationDeclinationEWT"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="East"/&gt;
 *     &lt;enumeration value="West"/&gt;
 *     &lt;enumeration value="True"/&gt;
 *     &lt;enumeration value="Grid"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StationDeclinationEWT", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum StationDeclinationEWT {


    /**
     * Declination is East of True North
     * 
     */
    @XmlEnumValue("East")
    EAST("East"),

    /**
     * Declination is East of West North
     * 
     */
    @XmlEnumValue("West")
    WEST("West"),

    /**
     * Station is oriented to True North in an area in which the local variation is not zero.
     * 
     */
    @XmlEnumValue("True")
    TRUE("True"),

    /**
     * Station is oriented to Grid North
     * 
     */
    @XmlEnumValue("Grid")
    GRID("Grid");
    private final String value;

    StationDeclinationEWT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StationDeclinationEWT fromValue(String v) {
        for (StationDeclinationEWT c: StationDeclinationEWT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
