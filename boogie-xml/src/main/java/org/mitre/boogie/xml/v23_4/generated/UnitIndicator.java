
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnitIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="UnitIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Msl"/&gt;
 *     &lt;enumeration value="Agl"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UnitIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum UnitIndicator {


    /**
     * MSL
     * 
     */
    @XmlEnumValue("Msl")
    MSL("Msl"),

    /**
     * AGL
     * 
     */
    @XmlEnumValue("Agl")
    AGL("Agl");
    private final String value;

    UnitIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UnitIndicator fromValue(String v) {
        for (UnitIndicator c: UnitIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
