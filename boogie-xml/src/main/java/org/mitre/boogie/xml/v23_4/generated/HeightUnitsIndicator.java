
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeightUnitsIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="HeightUnitsIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Feet"/&gt;
 *     &lt;enumeration value="Meters"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HeightUnitsIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum HeightUnitsIndicator {

    @XmlEnumValue("Feet")
    FEET("Feet"),
    @XmlEnumValue("Meters")
    METERS("Meters");
    private final String value;

    HeightUnitsIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HeightUnitsIndicator fromValue(String v) {
        for (HeightUnitsIndicator c: HeightUnitsIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
