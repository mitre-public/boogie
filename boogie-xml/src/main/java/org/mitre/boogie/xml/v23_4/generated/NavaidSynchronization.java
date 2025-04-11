
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NavaidSynchronization.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NavaidSynchronization"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Synchronous"/&gt;
 *     &lt;enumeration value="Asynchronous"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NavaidSynchronization", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NavaidSynchronization {


    /**
     * Synchronous
     * 
     */
    @XmlEnumValue("Synchronous")
    SYNCHRONOUS("Synchronous"),

    /**
     * ASynchronous
     * 
     */
    @XmlEnumValue("Asynchronous")
    ASYNCHRONOUS("Asynchronous"),

    /**
     * Unknown
     * 
     */
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    NavaidSynchronization(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NavaidSynchronization fromValue(String v) {
        for (NavaidSynchronization c: NavaidSynchronization.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
