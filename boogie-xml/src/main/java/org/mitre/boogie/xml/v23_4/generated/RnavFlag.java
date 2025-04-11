
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RnavFlag.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="RnavFlag"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Yes"/&gt;
 *     &lt;enumeration value="No"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RnavFlag", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum RnavFlag {


    /**
     * The field will indicate Yes, the procedure is an RNAV procedure
     * 
     */
    @XmlEnumValue("Yes")
    YES("Yes"),

    /**
     * The field will indicate No, the procedure is NOT an RNAV procedure
     * 
     */
    @XmlEnumValue("No")
    NO("No");
    private final String value;

    RnavFlag(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RnavFlag fromValue(String v) {
        for (RnavFlag c: RnavFlag.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
