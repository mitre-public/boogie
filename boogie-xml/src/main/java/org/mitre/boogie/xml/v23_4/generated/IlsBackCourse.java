
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IlsBackCourse.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="IlsBackCourse"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Usable"/&gt;
 *     &lt;enumeration value="Unusable"/&gt;
 *     &lt;enumeration value="Restricted"/&gt;
 *     &lt;enumeration value="Undefined"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "IlsBackCourse", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum IlsBackCourse {


    /**
     * Usable
     * 
     */
    @XmlEnumValue("Usable")
    USABLE("Usable"),

    /**
     * Unusable
     * 
     */
    @XmlEnumValue("Unusable")
    UNUSABLE("Unusable"),

    /**
     * Restricted
     * 
     */
    @XmlEnumValue("Restricted")
    RESTRICTED("Restricted"),

    /**
     * Undefined
     * 
     */
    @XmlEnumValue("Undefined")
    UNDEFINED("Undefined");
    private final String value;

    IlsBackCourse(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IlsBackCourse fromValue(String v) {
        for (IlsBackCourse c: IlsBackCourse.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
