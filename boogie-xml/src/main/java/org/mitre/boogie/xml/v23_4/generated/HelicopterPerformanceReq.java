
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HelicopterPerformanceReq.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="HelicopterPerformanceReq"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MultiEngine"/&gt;
 *     &lt;enumeration value="SingleEngineOnly"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HelicopterPerformanceReq", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum HelicopterPerformanceReq {

    @XmlEnumValue("MultiEngine")
    MULTI_ENGINE("MultiEngine"),
    @XmlEnumValue("SingleEngineOnly")
    SINGLE_ENGINE_ONLY("SingleEngineOnly"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    HelicopterPerformanceReq(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HelicopterPerformanceReq fromValue(String v) {
        for (HelicopterPerformanceReq c: HelicopterPerformanceReq.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
