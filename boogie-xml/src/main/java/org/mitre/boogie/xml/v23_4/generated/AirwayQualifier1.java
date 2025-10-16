
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AirwayQualifier1.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AirwayQualifier1"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="GnssRequired"/&gt;
 *     &lt;enumeration value="GnssOrDmeDmeIruRequired"/&gt;
 *     &lt;enumeration value="GnssDmeDmeIruOrDmeDmeRequired"/&gt;
 *     &lt;enumeration value="EquipmentRequirementsUnspecified"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirwayQualifier1", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AirwayQualifier1 {


    /**
     * GNSS required
     * 
     */
    @XmlEnumValue("GnssRequired")
    GNSS_REQUIRED("GnssRequired"),

    /**
     * GNSS or DME/DME/IRU Required
     * 
     */
    @XmlEnumValue("GnssOrDmeDmeIruRequired")
    GNSS_OR_DME_DME_IRU_REQUIRED("GnssOrDmeDmeIruRequired"),

    /**
     * GNSS, DME/DME/IRU or DME/DME Required
     * 
     */
    @XmlEnumValue("GnssDmeDmeIruOrDmeDmeRequired")
    GNSS_DME_DME_IRU_OR_DME_DME_REQUIRED("GnssDmeDmeIruOrDmeDmeRequired"),

    /**
     * Equipment requirements unspecified
     * 
     */
    @XmlEnumValue("EquipmentRequirementsUnspecified")
    EQUIPMENT_REQUIREMENTS_UNSPECIFIED("EquipmentRequirementsUnspecified");
    private final String value;

    AirwayQualifier1(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AirwayQualifier1 fromValue(String v) {
        for (AirwayQualifier1 c: AirwayQualifier1 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
