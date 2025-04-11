
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlternateType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="AlternateType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Airport"/&gt;
 *     &lt;enumeration value="CompanyRoute"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AlternateType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum AlternateType {


    /**
     * Airport is provided
     * 
     */
    @XmlEnumValue("Airport")
    AIRPORT("Airport"),

    /**
     * Company Route is provided
     * 
     */
    @XmlEnumValue("CompanyRoute")
    COMPANY_ROUTE("CompanyRoute");
    private final String value;

    AlternateType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AlternateType fromValue(String v) {
        for (AlternateType c: AlternateType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
