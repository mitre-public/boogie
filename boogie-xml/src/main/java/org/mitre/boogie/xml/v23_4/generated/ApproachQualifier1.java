
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApproachQualifier1.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ApproachQualifier1"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DmeRequired"/&gt;
 *     &lt;enumeration value="GpsRequiredDmeDmeNotAuthorized"/&gt;
 *     &lt;enumeration value="DmeNotRequired"/&gt;
 *     &lt;enumeration value="GnssRequired"/&gt;
 *     &lt;enumeration value="GnssOrDmeDmeRequired"/&gt;
 *     &lt;enumeration value="DmeDmeRequired"/&gt;
 *     &lt;enumeration value="RnavSensorNotSpecified"/&gt;
 *     &lt;enumeration value="VorDmeRnav"/&gt;
 *     &lt;enumeration value="RnavRequiresFasDataBlock"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ApproachQualifier1", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ApproachQualifier1 {


    /**
     * DME Required for Procedure
     * 
     */
    @XmlEnumValue("DmeRequired")
    DME_REQUIRED("DmeRequired"),

    /**
     * GPS (GNSS) required, DME/DME to RNP xx.x not authorized
     * 
     */
    @XmlEnumValue("GpsRequiredDmeDmeNotAuthorized")
    GPS_REQUIRED_DME_DME_NOT_AUTHORIZED("GpsRequiredDmeDmeNotAuthorized"),

    /**
     * DME Not Required for Procedure
     * 
     */
    @XmlEnumValue("DmeNotRequired")
    DME_NOT_REQUIRED("DmeNotRequired"),

    /**
     * GNSS Required
     * 
     */
    @XmlEnumValue("GnssRequired")
    GNSS_REQUIRED("GnssRequired"),

    /**
     * GPS (GNSS) or DME/DME to RNP xx.x  required
     * 
     */
    @XmlEnumValue("GnssOrDmeDmeRequired")
    GNSS_OR_DME_DME_REQUIRED("GnssOrDmeDmeRequired"),

    /**
     * DME/DME Required for Procedure
     * 
     */
    @XmlEnumValue("DmeDmeRequired")
    DME_DME_REQUIRED("DmeDmeRequired"),

    /**
     * RNAV, Sensor Not Specified
     * 
     */
    @XmlEnumValue("RnavSensorNotSpecified")
    RNAV_SENSOR_NOT_SPECIFIED("RnavSensorNotSpecified"),

    /**
     * VOR/DME RNAV
     * 
     */
    @XmlEnumValue("VorDmeRnav")
    VOR_DME_RNAV("VorDmeRnav"),

    /**
     * RNAV Procedure that Requires FAS Data Block
     * 
     */
    @XmlEnumValue("RnavRequiresFasDataBlock")
    RNAV_REQUIRES_FAS_DATA_BLOCK("RnavRequiresFasDataBlock");
    private final String value;

    ApproachQualifier1(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ApproachQualifier1 fromValue(String v) {
        for (ApproachQualifier1 c: ApproachQualifier1 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
