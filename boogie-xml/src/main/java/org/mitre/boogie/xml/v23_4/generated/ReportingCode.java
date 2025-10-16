
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReportingCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ReportingCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Required"/&gt;
 *     &lt;enumeration value="NotRequired"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ReportingCode", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum ReportingCode {


    /**
     * Position Report Required
     * 
     */
    @XmlEnumValue("Required")
    REQUIRED("Required"),

    /**
     * Position Report Not Required
     * 
     */
    @XmlEnumValue("NotRequired")
    NOT_REQUIRED("NotRequired");
    private final String value;

    ReportingCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReportingCode fromValue(String v) {
        for (ReportingCode c: ReportingCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
