
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MagneticTrueIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MagneticTrueIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Both"/&gt;
 *     &lt;enumeration value="True"/&gt;
 *     &lt;enumeration value="Magnetic"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MagneticTrueIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum MagneticTrueIndicator {


    /**
     * The field will be blank if details and procedures are provided in both magnetic and true for the airport. In such cases the individual detail or procedure records will contain the magnetic or true information
     * 
     */
    @XmlEnumValue("Both")
    BOTH("Both"),

    /**
     * if all detail and procedure for the airport are reported in true bearing
     * 
     */
    @XmlEnumValue("True")
    TRUE("True"),

    /**
     * if all detail and procedure for the airport are reported in magnetic bearing,
     * 
     */
    @XmlEnumValue("Magnetic")
    MAGNETIC("Magnetic");
    private final String value;

    MagneticTrueIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MagneticTrueIndicator fromValue(String v) {
        for (MagneticTrueIndicator c: MagneticTrueIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
