
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RunwaySurfaceCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RunwaySurfaceCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Hard"/&gt;
 *     &lt;enumeration value="Soft"/&gt;
 *     &lt;enumeration value="Water"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RunwaySurfaceCode", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RunwaySurfaceCode {


    /**
     * Hard Surface, for example, asphalt or concrete
     * 
     */
    @XmlEnumValue("Hard")
    HARD("Hard"),

    /**
     * Soft Surface, for example, gravel, grass or soil
     * 
     */
    @XmlEnumValue("Soft")
    SOFT("Soft"),

    /**
     * Water Runway
     * 
     */
    @XmlEnumValue("Water")
    WATER("Water"),

    /**
     * Undefined, surface material not provided in source
     * 
     */
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined");
    private final String value;

    RunwaySurfaceCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RunwaySurfaceCode fromValue(String v) {
        for (RunwaySurfaceCode c: RunwaySurfaceCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
