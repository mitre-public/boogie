
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LevelOfServiceAuthorized.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="LevelOfServiceAuthorized"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Authorized"/&gt;
 *     &lt;enumeration value="NotAuthorized"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LevelOfServiceAuthorized", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum LevelOfServiceAuthorized {


    /**
     * Designated Level of Service is authorized for the procedure
     * 
     */
    @XmlEnumValue("Authorized")
    AUTHORIZED("Authorized"),

    /**
     * Designated Level of Service is not authorized for the procedure
     * 
     */
    @XmlEnumValue("NotAuthorized")
    NOT_AUTHORIZED("NotAuthorized");
    private final String value;

    LevelOfServiceAuthorized(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LevelOfServiceAuthorized fromValue(String v) {
        for (LevelOfServiceAuthorized c: LevelOfServiceAuthorized.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
