
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NdbNavaidCoverage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NdbNavaidCoverage"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="HighPowerNdb"/&gt;
 *     &lt;enumeration value="LowPowerNdb"/&gt;
 *     &lt;enumeration value="Locator"/&gt;
 *     &lt;enumeration value="NDB"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NdbNavaidCoverage", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NdbNavaidCoverage {


    /**
     * High-powered NDB. Generally usable within 75NM of the facility at all altitudes.
     * 
     */
    @XmlEnumValue("HighPowerNdb")
    HIGH_POWER_NDB("HighPowerNdb"),

    /**
     * Low-powered NDB. Generally usable within 25NM of the facility at all altitude.
     * 
     */
    @XmlEnumValue("LowPowerNdb")
    LOW_POWER_NDB("LowPowerNdb"),

    /**
     * Locator. Generally usable within 15NM of the facility at all altitudes.
     * 
     */
    @XmlEnumValue("Locator")
    LOCATOR("Locator"),

    /**
     * NDB. Generally usable within 50NM of the facility at all altitude.
     * 
     */
    NDB("NDB");
    private final String value;

    NdbNavaidCoverage(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NdbNavaidCoverage fromValue(String v) {
        for (NdbNavaidCoverage c: NdbNavaidCoverage.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
