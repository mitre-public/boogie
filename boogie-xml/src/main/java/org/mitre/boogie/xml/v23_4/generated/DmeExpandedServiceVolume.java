
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DmeExpandedServiceVolume.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="DmeExpandedServiceVolume"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Nm130Feet18000"/&gt;
 *     &lt;enumeration value="Nm130Feet60000"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DmeExpandedServiceVolume", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum DmeExpandedServiceVolume {


    /**
     * Generally useable within 130 NM of the facility and up to 18000 feet with performance expanded through the volume (DL)
     * 
     */
    @XmlEnumValue("Nm130Feet18000")
    NM_130_FEET_18000("Nm130Feet18000"),

    /**
     * Generally useable within 130 NM of the facility and up to 60000 feet with performance expanded through the volume (DH)
     * 
     */
    @XmlEnumValue("Nm130Feet60000")
    NM_130_FEET_60000("Nm130Feet60000");
    private final String value;

    DmeExpandedServiceVolume(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DmeExpandedServiceVolume fromValue(String v) {
        for (DmeExpandedServiceVolume c: DmeExpandedServiceVolume.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
