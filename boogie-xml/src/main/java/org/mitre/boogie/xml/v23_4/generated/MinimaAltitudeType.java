
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MinimaAltitudeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MinimaAltitudeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DA"/&gt;
 *     &lt;enumeration value="MDA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MinimaAltitudeType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MinimaAltitudeType {


    /**
     * Altitude value is a Decision Altitude (DA)
     * 
     */
    DA,

    /**
     * Altitude value is a Minimum Descent Altitude (MDA)
     * 
     */
    MDA;

    public String value() {
        return name();
    }

    public static MinimaAltitudeType fromValue(String v) {
        return valueOf(v);
    }

}
