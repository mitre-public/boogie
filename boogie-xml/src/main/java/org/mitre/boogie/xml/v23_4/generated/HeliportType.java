
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeliportType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="HeliportType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Hospital"/&gt;
 *     &lt;enumeration value="OilRig"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *     &lt;enumeration value="NotProvided"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HeliportType", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum HeliportType {

    @XmlEnumValue("Hospital")
    HOSPITAL("Hospital"),
    @XmlEnumValue("OilRig")
    OIL_RIG("OilRig"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("NotProvided")
    NOT_PROVIDED("NotProvided");
    private final String value;

    HeliportType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HeliportType fromValue(String v) {
        for (HeliportType c: HeliportType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
