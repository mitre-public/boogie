
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VorRangePower.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="VorRangePower"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Nm25Feet12000"/&gt;
 *     &lt;enumeration value="Nm40Feet18000"/&gt;
 *     &lt;enumeration value="Nm130Feet60000Legacy"/&gt;
 *     &lt;enumeration value="Nm70Feet18000"/&gt;
 *     &lt;enumeration value="Nm130Feet60000VorMon"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VorRangePower", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum VorRangePower {


    /**
     * Generally useable within 25 NM of the facility and below 12000 feet (T)
     * 
     */
    @XmlEnumValue("Nm25Feet12000")
    NM_25_FEET_12000("Nm25Feet12000"),

    /**
     * Generally useable within 40 NM of the facility and up to 18000 feet (L)
     * 
     */
    @XmlEnumValue("Nm40Feet18000")
    NM_40_FEET_18000("Nm40Feet18000"),

    /**
     * Generally useable within 130 NM of the facility and up to 60000 feet (H)
     * 
     */
    @XmlEnumValue("Nm130Feet60000Legacy")
    NM_130_FEET_60000_LEGACY("Nm130Feet60000Legacy"),

    /**
     * Generally useable within 70 NM of the facility and up to 18000 feet with performance expanded through the volume (VL)
     * 
     */
    @XmlEnumValue("Nm70Feet18000")
    NM_70_FEET_18000("Nm70Feet18000"),

    /**
     * Generally useable within 130 NM of the facility and up to 60000 feet with performance expanded through the volume (VH)
     * 
     */
    @XmlEnumValue("Nm130Feet60000VorMon")
    NM_130_FEET_60000_VOR_MON("Nm130Feet60000VorMon");
    private final String value;

    VorRangePower(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VorRangePower fromValue(String v) {
        for (VorRangePower c: VorRangePower.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
