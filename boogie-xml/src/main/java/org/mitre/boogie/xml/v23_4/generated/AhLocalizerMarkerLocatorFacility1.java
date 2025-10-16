
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AhLocalizerMarkerLocatorFacility1.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AhLocalizerMarkerLocatorFacility1"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Ndb"/&gt;
 *     &lt;enumeration value="Sabh"/&gt;
 *     &lt;enumeration value="MarineBeacon"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AhLocalizerMarkerLocatorFacility1", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AhLocalizerMarkerLocatorFacility1 {


    /**
     * NDB
     * 
     */
    @XmlEnumValue("Ndb")
    NDB("Ndb"),

    /**
     * SABH
     * 
     */
    @XmlEnumValue("Sabh")
    SABH("Sabh"),

    /**
     * Marine Beacon.
     * 
     */
    @XmlEnumValue("MarineBeacon")
    MARINE_BEACON("MarineBeacon");
    private final String value;

    AhLocalizerMarkerLocatorFacility1(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AhLocalizerMarkerLocatorFacility1 fromValue(String v) {
        for (AhLocalizerMarkerLocatorFacility1 c: AhLocalizerMarkerLocatorFacility1 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
