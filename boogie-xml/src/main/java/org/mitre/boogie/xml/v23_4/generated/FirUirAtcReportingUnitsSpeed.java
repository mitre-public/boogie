
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirUirAtcReportingUnitsSpeed.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FirUirAtcReportingUnitsSpeed"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NotSpecified"/&gt;
 *     &lt;enumeration value="Knots"/&gt;
 *     &lt;enumeration value="Mach"/&gt;
 *     &lt;enumeration value="KilometersHr"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FirUirAtcReportingUnitsSpeed", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FirUirAtcReportingUnitsSpeed {


    /**
     * Not specified
     * 
     */
    @XmlEnumValue("NotSpecified")
    NOT_SPECIFIED("NotSpecified"),

    /**
     * TAS in Knots
     * 
     */
    @XmlEnumValue("Knots")
    KNOTS("Knots"),

    /**
     * TAS in Mach
     * 
     */
    @XmlEnumValue("Mach")
    MACH("Mach"),

    /**
     * TAS in Kilometers/hr
     * 
     */
    @XmlEnumValue("KilometersHr")
    KILOMETERS_HR("KilometersHr");
    private final String value;

    FirUirAtcReportingUnitsSpeed(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FirUirAtcReportingUnitsSpeed fromValue(String v) {
        for (FirUirAtcReportingUnitsSpeed c: FirUirAtcReportingUnitsSpeed.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
