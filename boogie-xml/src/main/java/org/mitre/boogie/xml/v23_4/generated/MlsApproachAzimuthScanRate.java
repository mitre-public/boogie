
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MlsApproachAzimuthScanRate.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MlsApproachAzimuthScanRate"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AzimuthNotAvailable"/&gt;
 *     &lt;enumeration value="AzimuthAvailable"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MlsApproachAzimuthScanRate", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MlsApproachAzimuthScanRate {


    /**
     * Where a high-rate approach azimuth guidance is not available, enter blank.
     * 
     */
    @XmlEnumValue("AzimuthNotAvailable")
    AZIMUTH_NOT_AVAILABLE("AzimuthNotAvailable"),

    /**
     * Where a high-rate approach azimuth guidance is available, enter “H,”
     * 
     */
    @XmlEnumValue("AzimuthAvailable")
    AZIMUTH_AVAILABLE("AzimuthAvailable");
    private final String value;

    MlsApproachAzimuthScanRate(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MlsApproachAzimuthScanRate fromValue(String v) {
        for (MlsApproachAzimuthScanRate c: MlsApproachAzimuthScanRate.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
