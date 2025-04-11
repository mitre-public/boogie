
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PublicMilitaryIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="PublicMilitaryIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Private"/&gt;
 *     &lt;enumeration value="Civil"/&gt;
 *     &lt;enumeration value="Military"/&gt;
 *     &lt;enumeration value="Joint"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PublicMilitaryIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum PublicMilitaryIndicator {


    /**
     * Airport/Heliport is not open to the public (private)
     * 
     */
    @XmlEnumValue("Private")
    PRIVATE("Private"),

    /**
     * Airport/Heliport is open to the public (civil)
     * 
     */
    @XmlEnumValue("Civil")
    CIVIL("Civil"),

    /**
     * Airport/Heliport is military airport
     * 
     */
    @XmlEnumValue("Military")
    MILITARY("Military"),

    /**
     * Airport is joint Civil and Military
     * 
     */
    @XmlEnumValue("Joint")
    JOINT("Joint");
    private final String value;

    PublicMilitaryIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PublicMilitaryIndicator fromValue(String v) {
        for (PublicMilitaryIndicator c: PublicMilitaryIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
