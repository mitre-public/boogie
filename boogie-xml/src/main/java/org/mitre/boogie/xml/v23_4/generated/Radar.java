
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Radar.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Radar"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PrimaryOrSecondary"/&gt;
 *     &lt;enumeration value="No"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Radar", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum Radar {


    /**
     * Primary or secondary Radar information is available to the service
     * 
     */
    @XmlEnumValue("PrimaryOrSecondary")
    PRIMARY_OR_SECONDARY("PrimaryOrSecondary"),

    /**
     * If the Service documentation specifically states that the service does not have access  to primary or secondary radar information
     * 
     */
    @XmlEnumValue("No")
    NO("No"),

    /**
     * If the Source documentation does not provide details on the radar information access for the services
     * 
     */
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    Radar(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Radar fromValue(String v) {
        for (Radar c: Radar.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
