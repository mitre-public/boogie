
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirUirIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="FirUirIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Fir"/&gt;
 *     &lt;enumeration value="Uir"/&gt;
 *     &lt;enumeration value="Combined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FirUirIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum FirUirIndicator {


    /**
     * FIR
     * 
     */
    @XmlEnumValue("Fir")
    FIR("Fir"),

    /**
     * UIR
     * 
     */
    @XmlEnumValue("Uir")
    UIR("Uir"),

    /**
     * Combined FIR/UIR
     * 
     */
    @XmlEnumValue("Combined")
    COMBINED("Combined");
    private final String value;

    FirUirIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FirUirIndicator fromValue(String v) {
        for (FirUirIndicator c: FirUirIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
