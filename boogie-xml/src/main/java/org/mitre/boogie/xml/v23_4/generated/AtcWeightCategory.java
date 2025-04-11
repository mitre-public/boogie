
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AtcWeightCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AtcWeightCategory"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Heavy"/&gt;
 *     &lt;enumeration value="Medium"/&gt;
 *     &lt;enumeration value="Light"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AtcWeightCategory", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AtcWeightCategory {


    /**
     * Heavy, all aircraft types of 136,000kg (300000LB) or more.
     * 
     */
    @XmlEnumValue("Heavy")
    HEAVY("Heavy"),

    /**
     * Medium, aircraft types less than 136,000kg (300,000LB) and more than 7,000kg (155,000LB).
     * 
     */
    @XmlEnumValue("Medium")
    MEDIUM("Medium"),

    /**
     * Light, aircraft types of 7,000kg (155,000LB) or less
     * 
     */
    @XmlEnumValue("Light")
    LIGHT("Light");
    private final String value;

    AtcWeightCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AtcWeightCategory fromValue(String v) {
        for (AtcWeightCategory c: AtcWeightCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
