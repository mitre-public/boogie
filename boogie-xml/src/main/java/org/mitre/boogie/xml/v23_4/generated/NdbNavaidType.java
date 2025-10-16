
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NdbNavaidType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NdbNavaidType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Ndb"/&gt;
 *     &lt;enumeration value="Sabh"/&gt;
 *     &lt;enumeration value="MarineBeacon"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NdbNavaidType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NdbNavaidType {


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

    NdbNavaidType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NdbNavaidType fromValue(String v) {
        for (NdbNavaidType c: NdbNavaidType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
